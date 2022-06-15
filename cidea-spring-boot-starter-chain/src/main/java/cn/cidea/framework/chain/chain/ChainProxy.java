package cn.cidea.framework.chain.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * 负责代理实现
 *
 * @author Charlotte
 */
public class ChainProxy<T> implements MethodInterceptor, BeanFactoryAware {

    protected Logger log = LoggerFactory.getLogger(ChainProxy.class);

    protected Class<T> interfaceClass;

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
        if (Object.class.equals(method.getDeclaringClass())) {
            // 跳过Object基础方法
            return method.invoke(this, args);
        }
        log.debug("call: {}#{}({})", obj.getClass(), method.getName(), Arrays.toString(method.getParameterTypes()));

        Map<String, T> beansOfType = beanFactory.getBeansOfType(interfaceClass);
        IChainLogService logService = beanFactory.getBeanProvider(IChainLogService.class).getIfAvailable();
        for (Map.Entry<String, T> entry : beansOfType.entrySet()) {
            T bean = entry.getValue();
            if (bean == obj) {
                // 跳过本代理对象
                continue;
            }
            if (bean instanceof IChain && !((IChain) bean).enabled()) {
                // 实现了开关接口
                // 未启用，跳过
                continue;
            }
            log.debug("call: beanName = {}", entry.getKey());
            // TODO Charlotte: 2022/3/9 线程池
            try {
                Object ret = method.invoke(bean, args);
                try {
                    if(logService != null){
                        logService.success(entry.getKey(), method, args, ret);
                    }
                } catch (Throwable e){
                    log.error("日志记录异常!", e);
                }
            } catch (Throwable e){
                if(e instanceof InvocationTargetException){
                    e = ((InvocationTargetException) e).getTargetException();
                }
                log.error("链路异常!", e);
                try {
                    if(logService != null){
                        logService.fail(entry.getKey(), method, args, e);
                    }
                } catch (Throwable t){
                    log.error("日志记录异常!", t);
                }
            }
        }
        // TODO Charlotte: 2022/3/4 如果有返回值，怎么处理
        return null;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public void setInterfaceClass(Class<T> interfaceClass) {
        this.interfaceClass = interfaceClass;
    }
}
