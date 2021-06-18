package com.charlotte.lab.thread.flow.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.Callable;

/**
 * @author Charlotte
 */
@AllArgsConstructor
public class NodeExcuteTask implements Callable {

    private Object parameter;
    @Getter
    private IFlowNodeInterface flowNodeInterface;

    private Context context;

    public Object execute(){
        Object o = flowNodeInterface.invokeNode(parameter, context);
        flowNodeInterface.afterInvoke(parameter, context);
        return o;
    }

    @Override
    public Object call() {
        return execute();
    }
}
