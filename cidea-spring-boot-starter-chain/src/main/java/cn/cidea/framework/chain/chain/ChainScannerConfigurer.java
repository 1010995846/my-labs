package cn.cidea.framework.chain.chain;

import cn.cidea.framework.chain.annotation.Chain;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;

import static org.springframework.util.Assert.notNull;

/**
 * @author Charlotte
 */
public class ChainScannerConfigurer
        implements BeanDefinitionRegistryPostProcessor, InitializingBean, ApplicationContextAware, BeanNameAware {

    private String basePackage;

    private Class<? extends ChainFactoryBean> factoryBeanClass = ChainFactoryBean.class;

    private Class<? extends Annotation> annotationClass = Chain.class;

    private BeanNameGenerator nameGenerator;

    private ApplicationContext applicationContext;

    private String beanName;

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
        // 构建一个Scanner，以下都是进行设置前文注解中的信息
        ClassPathChainScanner scanner = new ClassPathChainScanner(registry, this);
        scanner.setResourceLoader(this.applicationContext);
        scanner.setBeanNameGenerator(this.nameGenerator);
        // 这里是进行实践的扫描注册操作
        // StringUtils.tokenizeToStringArray是分给数组，匹配,或者;
        scanner.scan(
                StringUtils.tokenizeToStringArray(this.basePackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS));
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.basePackage, "Property 'basePackage' is required");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Class<? extends ChainFactoryBean> getFactoryBeanClass() {
        return factoryBeanClass;
    }

    public void setFactoryBeanClass(Class<? extends ChainFactoryBean> factoryBeanClass) {
        this.factoryBeanClass = factoryBeanClass;
    }

    public void setNameGenerator(BeanNameGenerator nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    public Class<? extends Annotation> getAnnotationClass() {
        return annotationClass;
    }

    public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }
}
