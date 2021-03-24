# demo

定义接口`IOrgService`及其各个实现类（以下简称分支）。每个实现类添加`@StrategyMaster`或者`@StrategyBranch`注解。`@StrategyBranch`注解设置路由的映射值。

![demo-class](\pic\demo.png)

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

创建代理类`OrgStrategyProxy`。

- 添加注解`@StategyRoute`。

- 继承`AbstractStrategyProxy`，重写`#getRouteKeys()`方法，返回内部属性`routeKey`的值。

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

# 效果

## 一、路由策略

测试。不断修改`OrgStrategyProxy`属性`routeKey`的值，修改后调用同一个接口方法`#getName()`。

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

结果：

![demo-result](\pic\demo-result.png)

# 使用说明

以DEMO为例，假定一个接口具有多个直接或间接的实现类（接口需为实现类的第一个接口声明）。

一、添加注解

选定其中一个实现类添加注解`@StrategyMaster`（有且仅有一个），该实现类会作为主分支以及默认调用分支。

其余实现类添加注解`@StrategyBranch`作为分支类，并设置注解属性`#value`作为路由的映射值，同一个接口映射值不允许重复。

分支类和主分支类均要在接口的包或子包下。

二、重写路由规则

创建路由策略类，添加注解`@StategyRoute`，继承`AbstractStrategyProxy`。重写`getRouteKeys()`方法，返回的值匹配上一步中`@StrategyBranch`注解的映射值。

三、与spring常规接口调用一致，通过自动注入、beanFactory、context等方式获取接口的实现类（主要目的在于获取带有`@StrategyMaster`注解类的实例），然后直接调用接口方法即可。

# 设计说明

在某些情况和环境等因素的影响下，同一个行为或许具有不同的操作。这些行为具象化就是接口及其各个实现。如何根据不同的策略选择不同的实现就是需要解决的问题。

将这个选择实现类的过程（以下统称路由策略）抽象出来，并通过这个过程调用具体实现，由此引申出代理模式。

## 代理

接口所有实现的调用都通过proxy代理类完成，在代理的接口方法执行过程中，会先调用路由策略方法`#route(...)`获取具体的实现类，然后调用具体实现方法。代理类虽然也实现了接口，但并无具体行为，仅仅做一个调用的路由中转站。

![proxy-class](\pic\proxy-class.png)

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

与静态代理实现不同，动态代理的代理类在运行过程中动态生成并实例化。本文以`cglib`为例，具体实现参照下文实现解析。

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
@Import({StrategyBranchDefinitionRegistrar.class, StrategyBeanPostProcessor.class})
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

### 原理

spring在初始化bean的时候，提供了两个自定义处理bean的钩子，`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization()`分别为bean初始化前后的处理。

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
        // before
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
        // after
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

`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization()`的处理区别不大，调用`#getBeanPostProcessors()`方法获取`BeanPostProcessor`接口的实现集合，执行`#postProcessBeforeInitialization()`或者`#postProcessAfterInitialization()`对bean进行处理，若有返回值则作为新的bean返回，用户在这两处均可对bean进行额外的处理。

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

### 实现

> `StrategyRouteHelper`是一个路由的工具类，作为分支服务的注册中心，包含获取分支类、获取分支实现、缓存，以及一些简单的判断方法。具体实现略过。

配置类`StrategyBeanPostProcessor`，实现了`BeanPostProcessor`接口，由`@StrategyMaster`注解中的`@Import`属性导入生效。

重写`BeanPostProcessor`接口的`#postProcessAfterInitialization()`方法，返回代理类替代bean注入spring。

```java
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (StrategyRouteHelper.isMaster(bean)) {
            return createProxy(bean);
        }
        return bean;
    }

    public Object createProxy(Object bean) {
        StrategyMaster strategyMaster = bean.getClass().getAnnotation(StrategyMaster.class);
        // AbstractStrategyProxy是实现了MethodInterceptor接口的抽象类。后文解析。
        Class<? extends AbstractStrategyProxy> proxyClass = strategyMaster.proxy();
        // 原型模式，一个代理类代理一个接口
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

上一小节创建代理类的过程中，返回了一个`AbstractStrategyProxy`类型的对象。`AbstractStrategyProxy`实现了spring cglib的接口`MethodInterceptor`。代理类执行方法时调用`MethodInterceptor#intercept()`。策略路由`AbstractStrategyProxy`在该方法中根据调用的接口和路由键匹配分支，若分支未初始化则用spring注入容器中初始化。

```java
@Slf4j
public abstract class AbstractStrategyProxy implements MethodInterceptor {

    private static final String STRATEGY_ROUTE_SERVICE_SUFFIX = "@Strategy";
    
    private static final String[] NULL_KEYS = new String[]{null};

    protected Object bean;

    protected DefaultListableBeanFactory beanFactory;

    /**
     * @param obj         执行类
     * @param method      执行方法
     * @param args        参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public final Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        log.debug("call: {}#{}({})", obj.getClass(), method.getName(), Arrays.toString(method.getParameterTypes()));
        // 获取routeKey。getRouteKeys()是抽象方法，用于重写，提供自定义的获取方案
        String[] routeKeys = getRouteKeys(obj, method, args, methodProxy);
        if(routeKeys == null){
            routeKeys = NULL_KEYS;
        }
        log.debug("routeKeys = {}", Arrays.toString(routeKeys));
        // 待执行bean和method的封装对象
        StrategyRouteHelper.Invocation invocationToUse;
        // 尝试获取缓存
        if ((invocationToUse = StrategyRouteHelper.getCache(routeKeys, method)) != null) {
            // 命中缓存
            return invocationToUse.invoke(args);
        }
        // 无缓存，开始解析
        // 尝试映射到对应的branchClass
        for (String routeKey : routeKeys) {
            Class serviceClassToUse = StrategyRouteHelper.getBranchClass(bean.getClass(), routeKey);
            if(serviceClassToUse == null){
                continue;
            }

            // 映射到了branchClass
            log.debug("branchClassToUse = {}", serviceClassToUse);
            String serviceNameToUse = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, serviceClassToUse.getSimpleName()) + STRATEGY_ROUTE_SERVICE_SUFFIX;

            // 避免并发时branch重复注入spring
            Object beanToUse;
            if (!beanFactory.containsBean(serviceNameToUse)) {
                synchronized (serviceClassToUse) {
                    if (!beanFactory.containsBean(serviceNameToUse)) {
                        // branch未注册时，注入spring，完成依赖
                        RootBeanDefinition beanDefinition = new RootBeanDefinition(serviceClassToUse);
                        beanFactory.registerBeanDefinition(serviceNameToUse, beanDefinition);
                    }
                }
            }
            beanToUse = beanFactory.getBean(serviceNameToUse);

            Method methodToUse = MethodUtils.getMatchingMethod(
                    serviceClassToUse, method.getName(), method.getParameterTypes());
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
        }
        if (invocationToUse == null){
            // 路由不存在对应的branchClass
            log.debug("call master service。");
            // 调用获取默认bean和method的方法，默认为代理的mainBean和method，可重写修改
            Object beanToUse = getDefaultBeanToUse(obj, method, args, methodProxy, routeKeys);
            Method methodToUse = getDefaultMethodToUse(obj, method, args, methodProxy, routeKeys);
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            for (String routeKey : routeKeys) {
                StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
            }
        }
        // invoke()时不可锁class，防止二次代理调用锁死
        Object result = invocationToUse.invoke(args);
        log.debug("strategy proxy finished。");
        return result;
    }

    public final void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public final void setBean(Object bean) {
        this.bean = bean;
    }

    protected Method getDefaultMethodToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return method;
    }

    protected Object getDefaultBeanToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return bean;
    }

    /**
     * 路由key，匹配对应接口/父类下属的分支{@link StrategyBranch#value()}
     *
     * @param obj
     * @param method
     * @param args
     * @param methodProxy
     * @return
     */
    protected abstract String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy);
}
```

- `AbstractStrategyProxy`不能直接使用，它提供了若干方法供用户重写。继承该类后，必须重写抽象方法`#getRouteKeys(...)`返回路由的映射值，映射值匹配branch注解中的值。也可重写`#getDefaultMethodToUse(...)`和`#getDefaultBeanToUse(...)`修改默认调用的bean和method。

- 用户自定义的策略路由继承`AbstractStrategyProxy`同时还需要添加注解`@StategyRoute`。`@StategyRoute`注解仅仅是一个复合注解，无其它作用。包含`@Service`和`@Scope`，使添加注解的类作为原型模式注入到spring容器中。

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Scope("prototype")
public @interface StategyRoute {
}
```

> 参照demo中的`OrgStrategyProxy`

## 分支注册



# 扩展

## 指定策略路由

若存在需要使用不同路由策略的场景，添加多个策略路由后，选定一个作为默认并添加`@Primary`注解。对需要使用指定策略路由的接口，将指定的路由策略的Class设置为其主分支注解`StrategyMaster`的`proxy`属性的值。

## 无接口代理

无接口代理也可实现，但所有分支类均要继承主分支类。

## 策略/模板模式

在分支代码的实现中，不同分支的代码往往具有较多的通用方法和操作。针对某个方法，可由主分支定义一个流程框架，定义各个钩子方法，分支类继承主分支类，选择性的重写钩子方法，即可提高代码复用性、减少代码冗余。

> 策略/模板模式在上文中已出现。
>
> - `AbstractAutowireCapableBeanFactory`类的`#initializeBean()`方法中提供了`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization()`，用户可在这两个方法中对bean进行修改、替换。
> - `AbstractStrategyProxy`提供了`#getRouteKeys()`抽象方法。用户可继承同一个`AbstractStrategyProxy`的模板，使用自定义逻辑改写`#getRouteKeys()`的返回值匹配branch。