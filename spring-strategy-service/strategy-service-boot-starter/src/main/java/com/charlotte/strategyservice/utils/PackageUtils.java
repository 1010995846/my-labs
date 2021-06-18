package com.charlotte.strategyservice.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.*;

/**
 * @author Charlotte
 */
@Slf4j
public class PackageUtils {

    private static final String RESOURCE_PATTERN = "**/*.class";

    public static Set<Class<?>> scanPackages(String[] basePackages, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) {
        return scanPackages(basePackages, PackageUtils.class.getClassLoader(), includeFilters, excludeFilters);
    }

    public static Set<Class<?>> scanPackages(String[] basePackages, ClassLoader classLoader, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) {
        Set<Class<?>> candidates = new HashSet<>();
        for (String pkg : basePackages) {
            if (StringUtils.isBlank(pkg)) {
                continue;
            }
            try {
                candidates.addAll(scanPackage(pkg, classLoader, includeFilters, excludeFilters));
            } catch (IOException e) {
                log.error("扫描指定包[{}]时出现异常", pkg);
                continue;
            }
        }
        return candidates;
    }

    /**
     * 获取符合要求的类
     * 参照{@link org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider#scanCandidateComponents(String)}
     * @param basePackage
     * @return
     * @throws IOException
     */
    public static List<Class<?>> scanPackage(String basePackage, ClassLoader classLoader, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("开始扫描指定包{}下的所有类" + basePackage);
        }
        List<Class<?>> candidates = new ArrayList<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + replaceDotByDelimiter(basePackage) + '/' + RESOURCE_PATTERN;
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourceLoader);
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            if (isCandidateResource(reader, readerFactory, includeFilters, excludeFilters)) {
                Class<?> candidateClass = transform(reader.getClassMetadata().getClassName(), classLoader);
                if (candidateClass != null) {
                    candidates.add(candidateClass);
                    log.debug("扫描到符合要求分支类:{}" + candidateClass.getName());
                }
            }
        }
        return candidates;
    }


    /**
     * 用"/"替换包路径中"."
     * @param path
     * @return
     */
    private static String replaceDotByDelimiter(String path) {
        return StringUtils.replace(path, ".", "/");
    }


    /**
     * @param reader
     * @param readerFactory
     * @param includeFilters
     * @param excludeFilters
     * @return
     * @throws IOException
     */
    protected static boolean isCandidateResource(MetadataReader reader, MetadataReaderFactory readerFactory, List<TypeFilter> includeFilters,
                                                 List<TypeFilter> excludeFilters) throws IOException {
        for (TypeFilter tf : excludeFilters) {
            if (tf.match(reader, readerFactory)) {
                return false;
            }
        }
        for (TypeFilter tf : includeFilters) {
            if (tf.match(reader, readerFactory)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param className
     * @return
     */
    private static Class<?> transform(String className, ClassLoader classLoader) {
        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className, classLoader);
        } catch (ClassNotFoundException e) {
            log.info("未找到指定分支类{%s}", className);
        }
        return clazz;
    }

}
