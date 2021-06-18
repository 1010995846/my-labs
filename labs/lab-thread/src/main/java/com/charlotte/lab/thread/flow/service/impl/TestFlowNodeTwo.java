package com.charlotte.lab.thread.flow.service.impl;

import com.charlotte.lab.thread.flow.service.ITestFlowNode;
import com.charlotte.lab.thread.flow.utils.Context;
import com.charlotte.lab.thread.flow.entity.TestParameter;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
public class TestFlowNodeTwo implements ITestFlowNode {

    @Override
    public String invokeNode(TestParameter testParameter, Context context) {
        System.out.println(Thread.currentThread() + "执行校验方法2: " + testParameter.getParamOne());
        return testParameter.getParamTwo();
    }

    @Override
    public void afterInvoke(TestParameter testParameter, Context context) {

    }

    @Override
    public String resultKey() {
        return "NodeTwo";
    }
}
