# 概述

该插件用于在Spring环境下，通过接口调用方法时，可通过自定义的策略动态分派到接口的各个分支实现。

**环境：**Spring2.2.0及以上

**使用场景：**在环境、出入参、需求等的影响下，接口的一种行为往往具有多套不同的实现。

> 在互联网医院项目中，不同平台、机构通常有很多个性化的需求。
>
> - 对接HIS接口。针对同一个接口方法，以上传HIS为例，由于HIS接口的不一致性，每个机构对接的接口方法都必然有一套独特实现。
> - 对不同机构而言，一个接口方法的行为不一致、行为数量不一致。典型如消息推送，HIS及需求的不一致导致推送的消息类型多样，消息内容多样。

# 演示

## demo

定义接口`IOrgService`及其各个实现类（以下简称分支）。每个实现类添加`@StrategyMaster`或者`@StrategyBranch`注解。`@StrategyBranch`注解设置路由的映射值。

类图：

![demo-class](pic\demo.png)

实现代码，包含一个`@StrategyMaster`（必须）和若干`@StrategyBranch`：

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

- 继承`AbstractStrategyProxy`，重写`#getRouteKeys()`方法，返回内部属性`routeKey`的值，方法返回值匹配`@StrategyBranch`注解的值。该步骤方法的实现可自定义。

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

## 效果

测试。修改`OrgStrategyProxy`属性`routeKey`的值，然后调用接口方法`#getName()`，循环。

```java
@Test
void route() {
    // 获取接口实现类
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

![demo-result](pic\demo-result.png)

# 快速使用

以DEMO为例，假定一个接口具有多个直接或间接的实现类（接口需为实现类的第一个接口声明）。

**一、添加注解**

选定其中一个实现类添加注解`@StrategyMaster`（有且仅有一个），该实现类会作为默认调用的主分支。

其余实现类添加注解`@StrategyBranch`作为分支类，并设置注解属性`#value`作为路由的映射值，同一个接口映射值不允许重复。

分支类和主分支类均要在接口的包或子包下。

**二、重写路由规则**

创建路由策略类，添加注解`@StategyRoute`，继承`AbstractStrategyProxy`。重写`getRouteKeys()`方法，返回的值匹配上一步中`@StrategyBranch`注解的映射值。

**三、调用**

与spring常规接口调用一致，通过自动注入、beanFactory、context等方式获取接口的实现类（主要目的在于获取带有`@StrategyMaster`注解类的实例），然后直接调用接口方法即可。

与常规调用的差异：实现类的注解由`@Service`替换为`@StrategyMaster`或`@StrategyBranch`。需要实现`AbstractStrategyProxy`。

# 设计说明

将调用接口方法策略分派到接口实现的这个过程抽象出来，称为**策略路由**。

为追求无感使用，以AOP为灵感，使用代理作为**策略路由**的实现方案，用代理实例替换在Spring容器中接口的原默认实现实例。

## 代理

接口的所有调用都通过代理控制，在本示例中通过代理控制对应接口各个实现类方法的访问。

代理按实现又分为静态代理与动态代理，二者思想并无区别。

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
		 * 伪代码，根据策略选择一个service实例
		 */
		return service;
	}
}
```

### 动态代理

与静态代理实现不同，动态代理的代理类在运行过程中动态生成并实例化。本文以以spring框架和`cglib`代理为例，展示一个策略路由的思路和实现方案。

### demo

`IOrdService`是要调用的接口，包含若干个直接或间接的实现类。

`OrgProxy`作为代理类，同样实现了接口`IOrdService`便于区分接口的调用方法。

- 内部方法`#route()`是**策略路由**的过程，确定要调用的分支实例。
- `IOrgService`接口的实现方法，区分各个接口方法，通过`#route`路由到实例后调用。

![proxy-class](pic\proxy-class.png)

# 实现解析

为追求无感使用，使用动态代理代理Spring容器中接口的主实例。由于动态代理不需要显式定义代理类，因此被代理的实现类一般也是接口的一个分支实现，同时需要作为接口的主实例，定义主实例注解`@StrategyMaster`。

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Primary
@Import({StrategyBeanPostProcessor.class})
public @interface StrategyMaster {

    /**
     * 指定策略路由，进阶中说明
     */
    Class<? extends AbstractStrategyProxy> proxy() default AbstractStrategyProxy.class;

}
```

- `@Service`注入Spring容器中，使其能够在注入过程中被代理；
- `@Primary`作为接口的主实例；
- `@Import`导入配置，后文说明；
- `#proxy`指定策略路由，后文说明；

接口其余分支在策略路由时需要有区分度，因此定义分支注解`@StrategyBranch`。

```java
import cn.cidea.framework.strategy.proxy.AbstractStrategyProxy;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface StrategyBranch {

    /**
     * 路由映射值
     * 对应{@link AbstractStrategyProxy#getRouteKeys(Object, Method, Object[], MethodProxy)}的返回值
     */
    String[] value();

}
```

- `#value`属性作为在策略路由过程中的匹配值。

> 考虑到实际生产环境可能不会有多少的分支实例被调用，因此不附加`@Service`等注解直接实例化注入Spring容器，改为在策略路由中匹配到时再注入。

## 动态代理类的生成、实例化和注入

主实例需要在注入Spring容器的过程中被代理，参照源码后得出方案。

### 解析

截取自`AbstractAutowireCapableBeanFactory`的部分代码。

下面是Spring默认使用的`beanFactory`，`DefaultListableBeanFactory`中，`#initializeBean()`初始化bean的流程。

- `invokeAwareMethods()`方法进行一些`Aware`接口方法的填充，如`BeanNameAware`、`BeanFactoryAware`、`BeanClassLoaderAware`；

- 调用`#applyBeanPostProcessorsBeforeInitialization`方法在bean初始化前进行处理；
- `#invokeInitMethods()`初始化bean，执行初始化方法等；
- 调用`#applyBeanPostProcessorsAfterInitialization`方法在bean初始完成后进行处理；

```java
// AbstractAutowireCapableBeanFactory.class，DefaultListableBeanFactory的父类
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
        // 初始化前置处理
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
        // 初始化后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }

    return wrappedBean;
}
```
Spring提供的扩展方案在初始化前后置处理中。

```java
// AbstractAutowireCapableBeanFactory.class
// bean初始化前置处理
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

// bean初始化后置处理
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

初始化前置处理和初始化后置处理的差异仅在与调用的`BeanPostProcessor`实例方法不同。

在方法中，循环调用processor的方法处理bean，每次处理返回的结果作为新的bean，返回null则中断处理。

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

到此即可得出方案，实现`BeanPostProcessor`接口，然后在处理方法中对标有`@StrategyMaster`注解Class的bean进行代理。

由于执行顺序的差异，一般在初始化方法执行完后再替换bean。前置一般进行一些属性的填充。

### 实现

定义配置类`StrategyBeanPostProcessor`，实现接口方法`BeanPostProcessor#postProcessAfterInitialization()`，使用上文的`@StrategyMaster`注解时导入生效。

```java
public class StrategyBeanPostProcessor implements BeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return proxyIfNecessary(bean);
    }

    private Object proxyIfNecessary(Object bean) {
        if(bean == null){
            return null;
        }
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        StrategyMaster strategyMaster = targetClass.getAnnotation(StrategyMaster.class);
        if(strategyMaster == null){
            return bean;
        }
        Class<? extends AbstractStrategyProxy> proxyClass = strategyMaster.proxy();
        // 获取代理实例的原型
        ObjectProvider<? extends AbstractStrategyProxy> beanProvider = beanFactory.getBeanProvider(proxyClass);
        AbstractStrategyProxy proxy = beanProvider.getIfAvailable();
        if(proxy == null){
            throw new NoSuchBeanDefinitionException(proxyClass);
        }
        proxy.setBean(bean);

        Enhancer enhancer = new Enhancer();
        enhancer.setInterfaces(ClassUtils.getAllInterfacesForClass(targetClass));
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback(proxy);
        enhancer.setCallbackType(proxy.getClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        Object proxyBean = enhancer.create();
        // 获取masterClass或masterClass接口，并扫包，注册branchClass
        StrategyRouteHelper.registerBranch(targetClass);
        return proxyBean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

}
```

> `StrategyRouteHelper`是一个工具类，作为分支服务的注册中心，包含获取分支类、分支实例、缓存，以及一些简单的判断方法。具体实现略过。

> 实现代理过程织入的方法比较多，本文只举出其中一种。
>
> 事实上直接使用AOP注解也可实现，AOP也是基于代理实现的。

## 动态代理路由的过程

上一小节实现并创建代理类的过程中，实例化了一个`AbstractStrategyProxy`类型的对象。

`AbstractStrategyProxy`是一个代理类，实现了Spring cglib的接口`MethodInterceptor`，代理调用类时调用`#MethodInterceptor#intercept()`，策略路由的调用过程在此处完成。

当调用被代理的主实例bean时，代理类拦截调用，并尝试根据路由策略重新分派到匹配的分支Bean，若分支未实例化则注入到Spring容器中完成实例化。

路由的策略方案提供了抽象方法`#getRouteKeys()`进行重写，方法返回的键值组匹配`@StrategyBranch#value`。

`AbstractStrategyProxy`的子类作为**策略路由代理类**以**原型模式**注入到Spring容器中，以便织入代理时取用。

```java
@Slf4j
public abstract class AbstractStrategyProxy implements MethodInterceptor, BeanFactoryAware {

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
        if ((invocationToUse = StrategyRouteHelper.getCache(routeKeys, method)) == null) {
            // 无缓存，开始解析
            // 尝试映射到对应的branchClass
            Class<?> targetClass = AopUtils.getTargetClass(bean);
            for (String routeKey : routeKeys) {
                Class serviceClassToUse = StrategyRouteHelper.getBranchClass(targetClass, routeKey);
                if(serviceClassToUse == null){
                    continue;
                }

                // 映射到了branchClass，进行初始化
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
                break;
            }
        }
        if (invocationToUse == null){
            // 兜底的默认操作，路由不存在对应的branchClass
            log.debug("call master service。");
            // 调用获取默认bean和method的方法，默认为代理的mainBean和method，可重写修改
            Object beanToUse = getDefaultBeanToUse(obj, method, args, methodProxy, routeKeys);
            Method methodToUse = getDefaultMethodToUse(obj, method, args, methodProxy, routeKeys);
            invocationToUse = new StrategyRouteHelper.Invocation(methodToUse, beanToUse);
            for (String routeKey : routeKeys) {
                StrategyRouteHelper.cacheBean(routeKey, method, invocationToUse);
            }
        }
        // invoke()时不可锁class，防止循环调用锁死
        Object result = invocationToUse.invoke(args);
        log.debug("strategy proxy finished。");
        return result;
    }

    public final void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public final void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    protected Method getDefaultMethodToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return method;
    }

    protected Object getDefaultBeanToUse(Object obj, Method method, Object[] args, MethodProxy methodProxy, String[] routeKeys) {
        return bean;
    }

    /**
     * 路由key，由用户重写自定义路由策略规则，匹配对应接口/父类下属的分支{@link StrategyBranch#value()}
     * @param obj
     * @param method
     * @param args
     * @param methodProxy
     * @return
     */
    protected abstract String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy);

}
```

- `AbstractStrategyProxy`是个抽象类不能直接使用，它提供了若干方法供用户重写。继承该类后，必须重写抽象方法`#getRouteKeys(...)`返回路由的映射值，映射值匹配branch注解中的值。也可重写`#getDefaultMethodToUse(...)`和`#getDefaultBeanToUse(...)`修改默认调用的bean和method，或者抛出异常禁用默认主实例。

- 用户自定义的策略路由继承`AbstractStrategyProxy`同时还需要添加注解`@StategyRoute`。`@StategyRoute`注解仅仅是一个复合注解，无其它作用。包含`@Service`和`@Scope`，使添加注解的类作为原型模式注入到spring容器中，一组分支实现对应一个代理类。

```java
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
@Scope("prototype")
public @interface StategyRoute {
}
```

> 使用实例参照demo中的`OrgStrategyProxy`

## 分支注册

> 分支注册的实现方案也比较多。最简单的是直接通过Spring组件注解进行管理。使用时从spring容器中获取。

本例中使用扫描包加载类的方案。

在上文`StrategyBeanPostProcessor`代理主实例Bean的方法中，调用了`#StrategyRouteHelper#registerBranch(...)`方法注册分支类，代码比较简单，就是扫描包、匹配类，然后按照接口分组、注册。

```java
// StrategyRouteHelper.class
public static void registerBranch(Class<?> masterClass) {
    // 获取策略的端口类
    Class portClass = getPortClass(masterClass);
    String className = portClass.getName();
    // 根据端口获取包路径，也是上文使用说明中，分支类要在接口包下的原因
    String[] basePackages = new String[]{className.substring(0, className.lastIndexOf('.'))};

    // 过滤器匹配Branch
    List<TypeFilter> includeFilters = new ArrayList<>();
    includeFilters.add(new AbstractClassTestingTypeFilter() {
        @Override
        public boolean match(ClassMetadata metadataReader) {
            try {
                Class clazz = ClassUtils.forName(metadataReader.getClassName(), portClass.getClassLoader());
                return isBranch(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return false;
        }
    });
    List<TypeFilter> excludeFilters = new ArrayList<>();

    // 包工具类，扫描包并返回符合条件的Class，具体实现请查阅源码
    Set<Class<?>> candidates = PackageUtils.scanPackages(basePackages, portClass.getClassLoader(), includeFilters, excludeFilters);
    if (candidates.isEmpty()) {
        log.info("扫描指定包[{}]时未发现符合条件的分支类", basePackages.toString());
        return;
    }

    for (Class<?> candidate : candidates) {
        addBranchClass(candidate);
    }
}

public static void addBranchClass(Class clazz) {
    StrategyBranch strategyBranch = AnnotationUtils.findAnnotation(clazz, StrategyBranch.class);
    if (strategyBranch == null) {
        return;
    }

    Class port = getPortClass(clazz);
    // 分支路由配置
    Map<String, Class> branchClassMap = serviceClassMap.get(port);
    if (branchClassMap == null) {
        branchClassMap = new HashMap<>();
        serviceClassMap.put(port, branchClassMap);
    }
    // 默认路由key：类名
    branchClassMap.put(clazz.getSimpleName(), clazz);
    // 注解配置的路由key
    for (String key : strategyBranch.value()) {
        branchClassMap.put(key, clazz);
    }
}
```

# 扩展

## 指定策略路由

若存在需要使用不同路由策略的场景，添加多个策略路由代理类后，选定一个作为默认策略，添加`@Primary`注解。对需要使用指定策略路由的接口，将指定的路由策略的Class设置为其主分支注解`StrategyMaster`的`proxy`属性的值。

在上文`StrategyBeanPostProcessor#createProxy()`代理实现中，会根据`@StrategyMaster`注解的`proxy`属性获取代理的指定Class，然后从容器中获取指定Class原型的实例。

## 无接口路由代理

路由代理也可使用无接口的类，但所有分支类均要直接或间接继承主分支类，以主分支类作为一个被代理的对象和调用的端口。

## 策略/模板模式

在分支代码的实现中，不同分支的代码往往具有较多相同的流程。针对某个方法，可由主分支定义一个流程框架，定义各个钩子方法或控制方法，分支类继承主分支类，选择性的重写，即可提高代码复用性、减少代码冗余。事实上，本DEMO设计初衷就是为模板模式服务的，最后经过迭代向上扩展成服务策略模式。

> 策略/模板模式在上文中已出现过。
>
> - 钩子：`AbstractAutowireCapableBeanFactory`类的`#initializeBean()`方法中提供了`#applyBeanPostProcessorsBeforeInitialization()`和`#applyBeanPostProcessorsAfterInitialization()`，用户可在这两个方法中使用自定义的`BeanPostProcessor`实现对bean进行修改、替换。
> - 模板：`AbstractStrategyProxy`提供了`#getRouteKeys()`抽象方法。用户继承父类模板`AbstractStrategyProxy`，使用自定义策略逻辑改写`#getRouteKeys()`方法的返回值，返回值匹配分支。用户继承的子类只需重写`#getRouteKeys()`路由的规则，不需要关心如何获取分支、如何调用，即可实现一个完整的路由流程，这些流程在模板父类中已经完成了。

# TODOLIST

- 策略类的多接口实现和注册
  - 方案一：注册bean的所有接口和父类。缺点：注册中心多余数据多一点，同时需要解决重复扫描。
  - 方案二：要注册的接口添加注解识别。缺点：对一些不能编辑的接口类没办法，如Spring提供的SPI，虽可通过冗余的继承间接实现，但又有点违背无感的初衷。

# 结语

本插件提供了一个根据自定义策略调用方法的自动路由功能，将显式的路由调用代码统一并抽离到了配置层面。但也仅仅提供了这个功能，并不是说使用本插件就能简化很多业务代码。在使用本插件的大多数场景中，业务分支代码的设计、规范对于策略化来说才是一个更为重要的基本功。