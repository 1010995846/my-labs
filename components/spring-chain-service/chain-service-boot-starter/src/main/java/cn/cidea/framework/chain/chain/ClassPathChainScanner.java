package cn.cidea.framework.chain.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
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
public class ClassPathChainScanner extends ClassPathBeanDefinitionScanner implements BeanFactoryAware {

    protected Logger log = LoggerFactory.getLogger(ClassPathChainScanner.class);

    private BeanFactory beanFactory;

    private ChainScannerConfigurer configurer;

    public ClassPathChainScanner(BeanDefinitionRegistry registry, ChainScannerConfigurer configurer) {
        super(registry, false);
        this.configurer = configurer;
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
            log.warn("No Chain service was found in '" + Arrays.toString(basePackages)
                    + "' package. Please check your configuration.");
        }

        return beanDefinitions;
    }

    private void processBeanDefinitions(Set<BeanDefinitionHolder> beanDefinitions) {
        GenericBeanDefinition definition;
        for (BeanDefinitionHolder holder : beanDefinitions) {
            definition = (GenericBeanDefinition) holder.getBeanDefinition();
            String beanClassName = definition.getBeanClassName();
            log.debug("Creating ChainFactoryBean with name '" + holder.getBeanName() + "' and '" + beanClassName
                    + "' interface");

            // 重点，设置构造参数，这里为接口的全限定名
            // 重点，这里设置为FactoryBean
            definition.getConstructorArgumentValues().addGenericArgumentValue(beanClassName);
            definition.setBeanClass(configurer.getFactoryBeanClass());

            // 设置参数
            definition.getPropertyValues().add("beanFactory", this.beanFactory);

            // 保证链路接口多实现的同时，默认调链路代理
            definition.setPrimary(true);
            definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        }

    }


    @Override
    protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
        return beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

}
