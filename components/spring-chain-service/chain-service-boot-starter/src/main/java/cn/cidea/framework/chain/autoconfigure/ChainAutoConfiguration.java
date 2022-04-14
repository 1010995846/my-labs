package cn.cidea.framework.chain.autoconfigure;

import cn.cidea.framework.chain.annotation.Chain;
import cn.cidea.framework.chain.chain.ChainFactoryBean;
import cn.cidea.framework.chain.chain.ChainScannerConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Charlotte
 */
@Configuration
@ConditionalOnMissingBean({ChainFactoryBean.class, ChainScannerConfigurer.class})
public class ChainAutoConfiguration  implements BeanFactoryAware, ImportBeanDefinitionRegistrar {

    protected Logger log = LoggerFactory.getLogger(ChainAutoConfiguration.class);

    private BeanFactory beanFactory;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        if (!AutoConfigurationPackages.has(this.beanFactory)) {
            log.debug("Could not determine auto-configuration package, automatic chain scanning disabled.");
            return;
        }

        log.debug("Searching for interface annotated with @Chain");
        List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
        if (log.isDebugEnabled()) {
            packages.forEach(pkg -> log.debug("Using auto-configuration base package '{}'", pkg));
        }

        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ChainScannerConfigurer.class);
        builder.addPropertyValue("annotationClass", Chain.class);
        builder.addPropertyValue("basePackage", StringUtils.collectionToCommaDelimitedString(packages));
        BeanWrapper beanWrapper = new BeanWrapperImpl(ChainScannerConfigurer.class);
        Stream.of(beanWrapper.getPropertyDescriptors())
                .filter(x -> x.getName().equals("lazyInitialization")).findAny()
                .ifPresent(x -> builder.addPropertyValue("lazyInitialization", "${mybatis.lazy-initialization:false}"));
        registry.registerBeanDefinition(ChainScannerConfigurer.class.getName(), builder.getBeanDefinition());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
