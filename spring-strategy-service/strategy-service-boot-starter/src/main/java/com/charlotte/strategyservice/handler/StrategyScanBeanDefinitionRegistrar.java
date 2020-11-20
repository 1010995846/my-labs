package com.charlotte.strategyservice.handler;

import com.charlotte.strategyservice.proxy.StrategyRouteHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 策略扫描器
 * 当基类实现了多个接口时,切记将实际的业务接口放在第一个
 *
 * @create 2017-12-22 23:16
 */
@Slf4j
public class StrategyScanBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    private static final String RESOURCE_PATTERN = "**/*.class";

    /**
     * @param importingClassMetadata
     * @param registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        // 扫描路径
        String[] basePackages = new String[2];

        // 扫描基类
        String className = importingClassMetadata.getClassName();
        basePackages[0] = className.substring(0, className.lastIndexOf('.'));
        if (importingClassMetadata.getInterfaceNames().length != 0) {
            // 若实现了接口，则再扫描接口
            String interfaceName = importingClassMetadata.getInterfaceNames()[0];
            String interfaceBasePackage = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
            if (!basePackages[0].contains(interfaceBasePackage)) {
                basePackages[1] = interfaceBasePackage;
            }
        }

        // 过滤器
        List<TypeFilter> includeFilters = new ArrayList<>();
        includeFilters.add(new AbstractClassTestingTypeFilter() {
            @Override
            public boolean match(ClassMetadata metadataReader) {
                try {
                    Class clazz = ClassUtils.forName(metadataReader.getClassName(), this.getClass().getClassLoader());
                    return StrategyRouteHelper.isBranch(clazz);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });
        List<TypeFilter> excludeFilters = new ArrayList<>();

        List<Class<?>> candidates = scanPackages(basePackages, includeFilters, excludeFilters);
        if (candidates.isEmpty()) {
            log.info("扫描指定包[{}]时未发现符合条件的分支类", basePackages.toString());
            return;
        }

        for (Class<?> candidate : candidates) {
            StrategyRouteHelper.addServiceClass(candidate);
        }
    }

    /**
     * @param basePackages
     * @param includeFilters
     * @param excludeFilters
     * @return
     */
    private List<Class<?>> scanPackages(String[] basePackages, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) {
        List<Class<?>> candidates = new ArrayList<>();
        for (String pkg : basePackages) {
            if (StringUtils.isBlank(pkg)) {
                continue;
            }
            try {
                candidates.addAll(findCandidateClasses(pkg, includeFilters, excludeFilters));
            } catch (IOException e) {
                log.error("扫描指定包[{}]时出现异常", pkg);
                continue;
            }
        }
        return candidates;
    }

    /**
     * 获取符合要求的Controller名称
     *
     * @param basePackage
     * @return
     * @throws IOException
     */
    private List<Class<?>> findCandidateClasses(String basePackage, List<TypeFilter> includeFilters, List<TypeFilter> excludeFilters) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("开始扫描指定包{}下的所有类" + basePackage);
        }
        List<Class<?>> candidates = new ArrayList<Class<?>>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + replaceDotByDelimiter(basePackage) + '/' + RESOURCE_PATTERN;
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        MetadataReaderFactory readerFactory = new SimpleMetadataReaderFactory(resourceLoader);
        Resource[] resources = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(packageSearchPath);
        for (Resource resource : resources) {
            MetadataReader reader = readerFactory.getMetadataReader(resource);
            if (isCandidateResource(reader, readerFactory, includeFilters, excludeFilters)) {
                Class<?> candidateClass = transform(reader.getClassMetadata().getClassName());
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
     *
     * @param path
     * @return
     */
    private String replaceDotByDelimiter(String path) {
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
    protected boolean isCandidateResource(MetadataReader reader, MetadataReaderFactory readerFactory, List<TypeFilter> includeFilters,
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
    private Class<?> transform(String className) {
        Class<?> clazz = null;
        try {
            clazz = ClassUtils.forName(className, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            log.info("未找到指定分支类{%s}", className);
        }
        return clazz;
    }

}