package cn.cidea.framework.chain.chain;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;

import static org.springframework.util.Assert.notNull;

/**
 * @author Charlotte
 */
public class ChainFactoryBean<T> implements InitializingBean, FactoryBean<T>, BeanFactoryAware {

    private Class<T> chainInterface;

    private BeanFactory beanFactory;

    public ChainFactoryBean(Class<T> chainInterface) {
        this.chainInterface = chainInterface;
    }

    @Override
    public T getObject() throws Exception {
        ChainProxy<T> proxy = new ChainProxy<>();
        proxy.setBeanFactory(beanFactory);
        proxy.setInterfaceClass(chainInterface);

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(new Class[]{chainInterface});
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        return (T) enhancer.create();
    }

    @Override
    public Class<?> getObjectType() {
        return this.chainInterface;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        notNull(this.chainInterface, "Property 'chainInterface' is required");
        notNull(this.beanFactory, "Property 'beanFactory' is required");
    }

    public void setChainInterface(Class<T> chainInterface) {
        this.chainInterface = chainInterface;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}
