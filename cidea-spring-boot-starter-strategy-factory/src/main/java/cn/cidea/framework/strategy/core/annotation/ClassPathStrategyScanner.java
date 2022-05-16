package cn.cidea.framework.strategy.core.annotation;

import cn.cidea.framework.strategy.core.factory.support.StrategyScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Set;

/**
 * @author Charlotte
 */
public class ClassPathStrategyScanner extends ClassPathBeanDefinitionScanner implements BeanFactoryAware {

    protected Logger log = LoggerFactory.getLogger(ClassPathStrategyScanner.class);

    private BeanFactory beanFactory;

    private StrategyScannerConfigurer configurer;

    public ClassPathStrategyScanner(BeanDefinitionRegistry registry, StrategyScannerConfigurer configurer) {
        super(registry, false);
        this.configurer = configurer;
        setBeanNameGenerator(configurer.getNameGenerator());
        registerDefaultFilters();
    }

    @Override
    protected void registerDefaultFilters() {
        addIncludeFilter(new AnnotationTypeFilter(configurer.getAnnotationClass()));
        addExcludeFilter((metadataReader, metadataReaderFactory) -> {
            String className = metadataReader.getClassMetadata().getClassName();
            return className.endsWith("package-info");
        });
    }

    @Override
    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        // 调用父类的doScan,将路径转为beanDefinition，然后再封装为BeanDefinitionHolder
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

        if (!beanDefinitions.isEmpty()) {
            // 这里对beanDefinition进一步加工
            processBeanDefinitions(beanDefinitions);
        } else {
            log.warn("No Strategy service was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            log.debug("Creating StrategyFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName
                    + "' interface");

            // 重点，设置构造参数，这里为接口的全限定名
            // 重点，这里设置为FactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(configurer.getFactoryBeanClass());

            // 设置参数
            definition.getPropertyValues().add("beanFactory", this.beanFactory);

            // 保证链路接口多实现的同时，默认调链路代理
            definition.setPrimary(true);
            // definition.setInitMethodName();
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }

    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return true;
    }

    @Override
    protected boolean isCompatible(BeanDefinition newDefinition, BeanDefinition existingDefinition) {
        return true;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
