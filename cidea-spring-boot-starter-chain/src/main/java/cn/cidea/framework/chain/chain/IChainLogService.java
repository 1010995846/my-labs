package cn.cidea.framework.chain.chain;

import java.lang.reflect.Method;

/**
 * （可选）链路日志回调接口
 * 当在Spring容器中存在实现此接口的类时，会进行调用
 * @author Charlotte
 */
public interface IChainLogService {

    void success(String beanName, Method method, Object[] args, Object ret);

    void fail(String beanName, Method method, Object[] args, Throwable exception);

}
