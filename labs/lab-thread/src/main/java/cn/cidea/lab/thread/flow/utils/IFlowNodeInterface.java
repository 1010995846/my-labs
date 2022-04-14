package cn.cidea.lab.thread.flow.utils;

/**
 * @author Charlotte
 */
public interface IFlowNodeInterface<T, P> {

    /**
     * 执行Node
     * @param parameter
     * @param context
     * @return
     */
    T invokeNode(P parameter, Context context);

    /**
     * Node回调
     * @param parameter
     * @param context
     */
    void afterInvoke(P parameter, Context context);

    /**
     * key
     * @return
     */
    String resultKey();
}
