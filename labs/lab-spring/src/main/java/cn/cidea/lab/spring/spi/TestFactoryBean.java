package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 一般情况下，Spring通过反射机制利用bean的class属性指定支线类去实例化bean
 * 在某些情况下，实例化Bean过程比较复杂，如果按照传统的方式，则需要在bean中提供大量的配置信息。配置方式的灵活性是受限的，这时采用编码的方式可能会得到一个简单的方案。
 * Spring为此提供了一个{@link org.springframework.beans.factory.FactoryBean}的工厂类接口，用户可以通过实现该接口定制实例化Bean的逻辑。
 *
 * FactoryBean接口对于Spring框架来说占用重要的地位，Spring自身就提供了70多个FactoryBean的实现。
 * 它们隐藏了实例化一些复杂bean的细节，给上层应用带来了便利。从Spring3.0开始，FactoryBean开始支持泛型，即接口声明改为FactoryBean<T>的形式
 * 使用场景：用户可以扩展这个类，来为要实例化的bean作一个代理，比如为该对象的所有的方法作一个拦截，在调用前后输出一行log，模仿ProxyFactoryBean的功能。
 * 实例：{@link org.mybatis.spring.mapper.MapperFactoryBean}
 * @author: CIdea
 */
@Slf4j
@Component
public class TestFactoryBean implements FactoryBean<TestFactoryBean.TestObject> {

    @Override
    public TestObject getObject() throws Exception {
        log.info("Custom FactoryBean#getObject()");
        return new TestObject();
    }

    @Override
    public Class<?> getObjectType() {
        log.info("Custom FactoryBean#getObjectType()");
        return TestObject.class;
    }

    public static class TestObject implements Serializable {

    }
}
