# 使用说明
## 配置类
继承`DefaultStrategyProxyAutoConfig`配置类并重写`#getStrategyProxy()`方法。
`#getStrategyProxy()`方法返回一个`AbstractStrategyProxy`对象，以原型模式注入到spring beanFactory中。

## AbstractStrategyProxy
分支的代理抽象类，定义了抽象方法`#getRouteKey()`用于获取路由到具体执行类的key值，使用时对其进行重写。
其它方法用于执行、判断。

## 策略类
### 主类
定义一个默认的实现类，添加注解`@StrategyMain`。作为被代理的类和路由默认实现类。
### 分支类
定义若干分支类，需继承默认实现类。在分支类上添加注解`@StrategyBranch`，路由时会将分支类的类名作为key值匹配，同时也可设置注解的`value`属性作为自定义的附加key值。
不建议给分支类添加spring组件注解，由于容器中存在主类的复数实现（自身和分支类），会导致自动注入时匹配失败，若执意添加，依赖注入时需指定具体的实现类。

# 实现解析
实现策略调用需要将策略分支类注入spring中容器，方便完成各种依赖。
##方案1：
对每个分支类添加spring组件注解，直接注入spring容器中。代理main时再根据接口/父类去spring容器中查找所有分支。
优点：逻辑、代码简单。
缺陷：spring容器中的策略类会包含多个实例。根据类查找策略bean时会查找到多个实例（main/branch），策略类不能直接根据类型获取相应的bean。
###方案1.1：
bean初始化时可以根据自定义注解set策略代理，解决自动注入的缺陷，手动获取依旧存在多实例的问题。
##方案2：
此方案主要解决多实例的问题。
延迟注入。在spring中初始化后移入自定义的容器中。
优点：spring容器中仅包含一个策略实例。
缺点：麻烦。需要一个实现类先注入spring容器中。需要自行将分支注入spring中，需要自定义容器。
###实现
####扫描
自行注入需要先识别出branch类。
`StrategyScanBeanDefinitionRegistrar`实现了接口`ImportBeanDefinitionRegistrar`。`ImportBeanDefinitionRegistrar`提供了一些对bean的额外操作。
在本示例中主要用于扫描branch。扫描到的branch类会被添加到`StrategyRouteHelper`工具类中并解析、结构化。
####代理
接口`BeanPostProcessor`提供了两个方法`#postProcessBeforeInitialization(Object, String)`和`#postProcessAfterInitialization(Object, String)`。
在spring bean的初始化前后会调用接口的所有实现，对bean进行处理。一般代理如AOP也是在此进行处理。
示例中定义的`StrategyBeanPostProcessor`类，用来创建初始化并返回策略代理类。
####代理类
`DefaultStrategyProxyAutoConfig`是一个注入原型策略代理示例的默认配置。通过重写`#getStrategyProxy()`方法，返回自定义的抽象类`AbstractStrategyProxy`实现类。
抽象类`AbstractStrategyProxy`提供了`#getRouteKey(...)`方法进行重写。