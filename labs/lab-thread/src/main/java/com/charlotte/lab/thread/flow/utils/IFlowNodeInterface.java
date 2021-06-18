package com.charlotte.lab.thread.flow.utils;

import com.charlotte.lab.thread.flow.utils.Context;

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
