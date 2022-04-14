package cn.cidea.lab.thread.flow.service.impl;

import cn.cidea.lab.thread.flow.service.ITestFlowNode;
import cn.cidea.lab.thread.flow.utils.Context;
import cn.cidea.lab.thread.flow.entity.TestParameter;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
public class TestFlowNodeOne implements ITestFlowNode {

    @Override
    public String invokeNode(TestParameter testParameter, Context context) {
        System.out.println(Thread.currentThread() + "执行检验方法1: " + testParameter.getParamOne());
        return testParameter.getParamOne();
    }

    @Override
    public void afterInvoke(TestParameter testParameter, Context context) {

    }

    @Override
    public String resultKey() {
        return "NodeOne";
    }
}
