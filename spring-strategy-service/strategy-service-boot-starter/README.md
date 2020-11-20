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
