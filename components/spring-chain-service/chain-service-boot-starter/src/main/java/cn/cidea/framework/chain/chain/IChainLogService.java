package cn.cidea.framework.chain.chain;

import java.lang.reflect.Method;

/**
 * @author Charlotte
 * （可选）链路日志回调接口
 */
public interface IChainLogService {

    void success(String beanName, Method method, Object[] args, Object ret);

    void fail(String beanName, Method method, Object[] args, Throwable exception);

}
