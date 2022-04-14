package cn.cidea.framework.chain.chain;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;

/**
 * @author Charlotte
 */
public class ChainBeanNameGenerator extends DefaultBeanNameGenerator {

    private static final String NAME_SUFFIX = "$chain";

    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        return super.generateBeanName(definition, registry) + NAME_SUFFIX;
    }
}
