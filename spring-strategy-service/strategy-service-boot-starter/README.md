# DEMO

定义接口`IOrgService`及其各个实现类（以下简称分支）。每个实现类添加`@StrategyMaster`或者`@StrategyBranch`注解。`@StrategyBranch`注解设置路由的映射值。

![image-20210322133629080](C:\Users\Charlotte\AppData\Roaming\Typora\typora-user-images\image-20210322133629080.png)

```java
@StrategyMaster
public class OrgService implements IOrgService {
    @Override
    public String getName() {
        return "defaultOrg";
    }
}

@StrategyBranch("ext")
public class ExtService implements IOrgService {
    @Override
    public String getName() {
        return "ext";
    }
}

@StrategyBranch("hosp")
public class HospService extends OrgService {
    @Override
    public String getName() {
        return "hosp";
    }
}

@StrategyBranch("school")
public class SchoolService extends OrgService {

    @Override
    public String getName() {
        return "school";
    }
}
```

# 效果

## 一、路由策略

创建代理类`OrgStrategyProxy`，添加注解`@StategyRoute`，继承`AbstractStrategyProxy`。重写`getRouteKeys()`方法，返回内部属性`routeKey`的值。

```java
@StategyRoute
public class OrgStrategyProxy extends AbstractStrategyProxy {
    public static ThreadLocal<String> routeKey = new ThreadLocal<>();

    @Override
    protected String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy) {
        // 返回routeKey
        return new String[]{routeKey.get()};
    }
}
```

测试。修改`OrgStrategyProxy`属性`routeKey`的值，并调用同一个接口方法`#getName()`。

```java
@Test
void route() {
    IOrgService implBean = beanFactory.getBean(IOrgService.class);
    OrgStrategyProxy.routeKey.set("school");
    System.out.println("name = " + implBean.getName());// 打印school
    OrgStrategyProxy.routeKey.set("hosp");
    System.out.println("name = " + implBean.getName());// 打印hosp
    System.out.println("name = " + implBean.getName());// 打印hosp
    OrgStrategyProxy.routeKey.set("ext");
    System.out.println("name = " + implBean.getName());// 打印ext
    OrgStrategyProxy.routeKey.remove();
    System.out.println("name = " + implBean.getName());// 打印defaultOrg
}
```

结果

![image-20210322152139462](C:\Users\Charlotte\AppData\Roaming\Typora\typora-user-images\image-20210322152139462.png)

# 使用说明

以DEMO为例，假定一个接口具有多个直接或间接的实现类（接口需为实现类的第一个接口声明）。

一、添加注解

（必需，有且仅有一个）选定其中一个实现类添加注解`@StrategyMaster`，该实现类会作为主分支以及默认调用分支。

其余实现类添加注解`@StrategyBranch`，并设置注解属性`#value`作为路由的映射值，同一个接口映射值不允许重复。

二、重写路由规则

（必需）创建路由策略类，添加注解`@StategyRoute`，继承`AbstractStrategyProxy`。重写`getRouteKeys()`方法，返回的值匹配上一步中`@StrategyBranch`注解的映射值。

三、与spring常规接口调用一致，通过自动注入、beanFactory、context等方式获取接口的实现类（主要目的在于获取带有`@StrategyMaster`注解类的实例），然后直接调用接口方法即可。

# 设计说明

在某些情况和环境等因素的影响下，同一个行为或许具有不同的操作。这些行为具象化就是接口及其各个实现。如何根据不同的策略选择不同的实现就是需要解决的问题。

将这个选择实现类的过程（以下统称路由策略）抽象出来，并通过这个过程调用具体实现，由此引申出代理模式。

## 代理

接口所有实现的调用都通过proxy代理类完成，在代理的接口方法执行过程中，会先调用路由策略方法`#route(...)`获取具体的实现类，然后调用具体实现方法。代理类虽然也实现了接口，但并无具体行为，仅仅做一个调用的路由中转站。

![image-20210323111101088](C:\Users\Charlotte\AppData\Roaming\Typora\typora-user-images\image-20210323111101088.png)

代理又分为静态代理与动态代理，二者代理的思路并无区别，仅实现及应用存在较大差异。

### 静态代理

直接添加一个实现了目标接口的类作为代理类。类是静态编译的。若需要使用spring注入等功能，则添加注解`@Service`和`@Primary`。

> `@Primary`注解会以该实例作为接口的主实现。即使spring容器中存在接口的多个实例，也可直接通过接口获取被`@Primary`注解标记的实例。

```java
public class OrgProxy implements IOrgService{
	
    @Override
    public String getName(){
		return route().getName();
	}
	
	private IOrgService route(){
		IOrgService service;
		/**
		 * 选择service
		 */
		return service;
	}
}
```

### 动态代理

与静态代理实现不同，代理类在运行过程中动态生成并实例化。本文以`cglib`为例，具体实现参照下文实现解析。

# 实现解析

为了不在业务代码中添加过多的静态代理类和显式路由代码，选定动态代理作为实现方案。

首先区分需要策略路由的接口，使用注解标记实现类并设置分支Branch的映射值。同时需要选定一个分支类作为被代理的基类，作为Master主分支，Master代理通过spring注解进行管理。

因此有如下注解定义。

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyBranch {
    
    /**
     * 路由映射值
     * 对应{@link com.charlotte.strategyservice.proxy.AbstractStrategyProxy#getRouteKeys(Object, Method, Object[], MethodProxy)}的返回值
     */
    String[] value();
    
}
```

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Primary
@Service
@Import({StrategyScanBeanDefinitionRegistrar.class, StrategyBeanPostProcessor.class})
public @interface StrategyMaster {

    /**
     * 指定策略路由，进阶中说明
     */
    Class<? extends AbstractStrategyProxy> proxy() default AbstractStrategyProxy.class;

}
```

`@StrategyBranch`标记分支类。

`@StrategyMaster`标记主分支，相比`@StrategyBranch`，增加了`@Service`、`@Primary`注解，用于作为接口的主实现服务注入spring容器中。`@Import`注解用于导入策略路由所需要的配置。

## 动态代理类的生成、实例化和注入

spring在初始化bean的时候，提供了两个自定义初始化bean的钩子，`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization()`分别为bean初始化前后的处理。

```java
// AbstractAutowireCapableBeanFactory.class
protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
    if (System.getSecurityManager() != null) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
            invokeAwareMethods(beanName, bean);
            return null;
        }, getAccessControlContext());
    }
    else {
        invokeAwareMethods(beanName, bean);
    }

    Object wrappedBean = bean;
    if (mbd == null || !mbd.isSynthetic()) {
        // 前置
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
    }

    try {
        // bean初始化方法
        invokeInitMethods(beanName, wrappedBean, mbd);
    }
    catch (Throwable ex) {
        throw new BeanCreationException(
            (mbd != null ? mbd.getResourceDescription() : null),
            beanName, "Invocation of init method failed", ex);
    }
    if (mbd == null || !mbd.isSynthetic()) {
        // 后置
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}

@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
    throws BeansException {

    Object result = existingBean;
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
        Object current = processor.postProcessBeforeInitialization(result, beanName);
        if (current == null) {
            return result;
        }
        result = current;
    }
    return result;
}

@Override
public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
    throws BeansException {

    Object result = existingBean;
    for (BeanPostProcessor processor : getBeanPostProcessors()) {
        Object current = processor.postProcessAfterInitialization(result, beanName);
        if (current == null) {
            return result;
        }
        result = current;
    }
    return result;
}

public List<BeanPostProcessor> getBeanPostProcessors() {
    return this.beanPostProcessors;
}
```

`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization`的处理区别不大，调用`#getBeanPostProcessors()`方法获取`BeanPostProcessor`接口的实现集合，执行`#postProcessBeforeInitialization()`或者`#postProcessAfterInitialization()`对bean进行处理，若有返回值则作为新的bean返回，用户在这两处均可对bean进行额外的处理。

由此，在`#postProcessAfterInitialization()`方法中返回代理类，即可用代理类替换需要代理的Master。

`BeanPostProcessor`接口，包含上述两个前/后置处理bean的方法。

```java
public interface BeanPostProcessor {

	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
    
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
```

`StrategyBeanPostProcessor`，实现`BeanPostProcessor`接口，由`@StrategyMaster`注解中的`@Import`属性导入生效。

```java
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StrategyRouteHelper.isMaster(bean) && !StrategyRouteHelper.isBranch(bean)) {
            return createProxy(bean);
        }
        return bean;
    }

    public Object createProxy(Object bean) {
        StrategyMaster strategyMaster = bean.getClass().getAnnotation(StrategyMaster.class);
        Class<? extends AbstractStrategyProxy> proxyClass = strategyMaster.proxy();
        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(proxyClass);
        AbstractStrategyProxy proxy = beanProvider.getIfAvailable();
        if(proxy == null){
            throw new NoSuchBeanDefinitionException(proxyClass);
        }
        proxy.setBean(bean);
        proxy.setBeanFactory(beanFactory);

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(ClassUtils.getAllInterfaces(bean));
        enhancer.setSuperclass(AopUtils.getTargetClass(bean));
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        Object proxyBean = enhancer.create();
        return proxyBean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
```



## 动态代理路由的过程





# 进阶

## 指定策略路由

若存在需要使用不同路由策略的场景，添加多个策略路由后，选定一个作为默认并添加`@Primary`注解。对需要使用指定策略路由的接口，将指定的路由策略的Class设置为其主分支注解`StrategyMaster`的`proxy`属性的值。

## 策略/模板