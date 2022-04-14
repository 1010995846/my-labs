package cn.cidea.lab.thread.flow;

import cn.cidea.lab.thread.flow.service.impl.TestFlowNodeOne;
import cn.cidea.lab.thread.flow.service.impl.TestFlowNodeTwo;
import cn.cidea.lab.thread.flow.entity.TestParameter;
import cn.cidea.lab.thread.flow.utils.Context;
import cn.cidea.lab.thread.flow.utils.TaskFlow;
import cn.cidea.lab.thread.flow.utils.TaskFlowEngine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Map;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan("com.charlotte.lab.thread.flow.*")
public class Application {

    public static TaskFlow taskFlow = new TaskFlow();

    static {
        taskFlow.add(TestFlowNodeOne.class, new TaskFlow.NodeConf());
        taskFlow.add(TestFlowNodeTwo.class, new TaskFlow.NodeConf());
        taskFlow.add("three", TestFlowNodeOne.class, new TaskFlow.NodeConf());
        taskFlow.add("three", TestFlowNodeTwo.class, new TaskFlow.NodeConf());
    }


    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Application.class, args);

        TaskFlowEngine engine = applicationContext.getBean(TaskFlowEngine.class);

        TestParameter testParameter = new TestParameter();
        testParameter.setParamOne("111");
        testParameter.setParamTwo("222");

        Context context = new Context();

        engine.execute(taskFlow, testParameter, context);
        Map<String, Object> adaptorMap = context.getAdaptorMap();

        for (Map.Entry<String, Object> entry : adaptorMap.entrySet()) {
            System.out.println("result: " + entry.getKey() + " = " + entry.getValue());
        }

    }

}
