package cn.cidea.lab.proxy.cglib;

import cn.cidea.lab.proxy.service.DogService;
import cn.cidea.lab.proxy.service.IAniService;
import org.springframework.cglib.proxy.*;

import java.lang.reflect.Method;

/**
 * @author Charlotte
 */
public class CglibProxy implements MethodInterceptor {

    private volatile static int counter;
    private int serialNumber;

    public CglibProxy() {
        serialNumber = ++counter;
    }

    /**
     * @param obj         执行类
     * @param method      执行方法
     * @param args        参数
     * @param methodProxy 代理方法
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println(serialNumber + "号cglib代理调用前。。。class = " + obj.getClass() + ", method = " + method.getName());
        Object ret = methodProxy.invokeSuper(obj, args);
        System.out.println(serialNumber + "号cglib代理调用后。。。class = " + obj.getClass() + ", method = " + method.getName());
        return ret;
    }

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        Callback[] callbacks = new Callback[]{
                // 一号Callback
                new CglibProxy(),
                // 二号Callback
                new CglibProxy()};
        Class[] callbackTypes = new Class[callbacks.length];
        for (int i = 0; i < callbacks.length; i++) {
            callbackTypes[i] = callbacks[i].getClass();
        }
        enhancer.setCallbacks(callbacks);
        enhancer.setCallbackTypes(callbackTypes);
        // Callback过滤器，根据method匹配执行的Callback
        // 与Filter不同，匹配到后不会按照序号继续执行后面的Callback
        // Callbacks有且仅有一个且未设置此项时，默认对象返回0
        // Callbacks为复数时，必须设置此项
        enhancer.setCallbackFilter(method -> {
            if ("run".equals(method.getName())) {
                // 执行2号Callback
                return 1;
            }
            // 其余方法执行1号Callback
            return 0;
        });
        // 代理对象的父类实现superclass，由于代理对象没有方法体，无法直接执行非基础方法，因此需要通过指定superclass继承方法的实现。
        // 大多数情况下执行superclass的方法，methodProxy.invokeSuper(obj, args)
        enhancer.setSuperclass(DogService.class);
        // 代理对象实现的接口
        enhancer.setInterfaces(new Class[]{IAniService.class});
        // TODO Charlotte: 2020/12/21 namingPolicy说明
//        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        // TODO Charlotte: 2020/12/21 strategy说明
//        enhancer.setStrategy();

        IAniService service = (IAniService) enhancer.create();
        for (int i = 0; i < 10; i++) {
//            service.run();
            service.hashCode();
        }
    }

}
