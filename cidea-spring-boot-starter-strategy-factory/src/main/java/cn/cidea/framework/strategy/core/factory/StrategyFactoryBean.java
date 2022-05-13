package cn.cidea.framework.strategy.core.factory;

import cn.cidea.framework.strategy.core.factory.proxy.StrategyProxy;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;

import static org.springframework.util.Assert.notNull;

/**
 * @author Charlotte
 */
public class StrategyFactoryBean<T> implements InitializingBean, FactoryBean<T>, BeanFactoryAware {

    private Class<T> port;

    private BeanFactory beanFactory;

    public StrategyFactoryBean(Class<T> port) {
        this.port = port;
    }

    @Override
    public T getObject() throws Exception {
        StrategyProxy proxy = new StrategyProxy(port, (DefaultListableBeanFactory) beanFactory);

        Enhancer enhancer = new Enhancer();
        if(port.isInterface()){
            enhancer.setInterfaces(new Class[]{port});
        } else {
            enhancer.setInterfaces(new Class[]{});
            enhancer.setSuperclass(port);
        }
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        return (T) enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        return this.port;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.port, "Property 'chainInterface' is required");
        notNull(this.beanFactory, "Property 'beanFactory' is required");
    }

    public void setPort(Class<T> port) {
        this.port = port;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
