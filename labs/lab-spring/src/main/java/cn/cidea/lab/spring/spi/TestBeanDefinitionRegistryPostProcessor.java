package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 在读取项目中的beanDefinition之后执行，提供一个补充的扩展点
 * 使用场景：你可以在这里动态注册自己的beanDefinition，可以加载classpath之外的bean
 * 例如spring{@link org.springframework.context.annotation.ComponentScan}
 * mybatis{@link org.mybatis.spring.mapper.MapperScannerConfigurer}去加载路径下的Mapper接口（未标记{@link org.apache.ibatis.annotations.Mapper}）
 * @author: CIdea
 */
@Slf4j
@Component
public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        log.info("Custom postProcessBeanDefinitionRegistry, beanDefinitionNames = {}", registry.getBeanDefinitionNames());
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }
}
