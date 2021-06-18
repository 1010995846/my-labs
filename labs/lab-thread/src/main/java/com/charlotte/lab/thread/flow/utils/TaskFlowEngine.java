package com.charlotte.lab.thread.flow.utils;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * @author Charlotte
 */
@Component
public class TaskFlowEngine implements ApplicationContextAware {

    @Getter
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    // 线程池
    public static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 60L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(500),
            new ThreadPoolTaskExecutor(),
            new ThreadPoolExecutor.AbortPolicy());

    public Map<? extends Class<? extends IFlowNodeInterface>, IFlowNodeInterface> getFlowNode() {
        Map<String, IFlowNodeInterface> flowNodeMap = applicationContext.getBeansOfType(IFlowNodeInterface.class);
        Map<? extends Class<? extends IFlowNodeInterface>, IFlowNodeInterface> collect = flowNodeMap.values().stream().collect(Collectors.toMap(c -> c.getClass(), c -> c));
        return collect;
    }

    public void execute(TaskFlow taskFlow, Object parameter, Context context) {
        Map<? extends Class<? extends IFlowNodeInterface>, IFlowNodeInterface> flowNodeMap = getFlowNode();

        Map<String, Map<Class<? extends IFlowNodeInterface>, TaskFlow.NodeConf>> groupMap = taskFlow.getNodeMap();
        for (Map.Entry<String, Map<Class<? extends IFlowNodeInterface>, TaskFlow.NodeConf>> groupEntry : groupMap.entrySet()) {

            boolean isThrow = false;
            String groupName = groupEntry.getKey();
            Map<Class<? extends IFlowNodeInterface>, TaskFlow.NodeConf> confMap = groupEntry.getValue();
            if (confMap == null) {
                continue;
            }
            if (groupMap.size() == 1 && confMap.size() == 1) {
                // 单任务，串行就好
                IFlowNodeInterface detailNode = flowNodeMap.get(confMap.keySet().iterator().next());
                NodeExcuteTask nodeExcuteTask = new NodeExcuteTask(parameter, detailNode, context);
                try {
                    Object result = nodeExcuteTask.execute();
                    context.getAdaptorMap().put(detailNode.resultKey(), result);
                } catch (Throwable t) {
                    isThrow = true;
                }
            } else {
                Map<NodeExcuteTask, Future> taskMap = new HashMap<>();
                for (Class<? extends IFlowNodeInterface> nodeClass : confMap.keySet()) {
                    IFlowNodeInterface detailNode = flowNodeMap.get(nodeClass);
                    NodeExcuteTask nodeExcuteTask = new NodeExcuteTask(parameter, detailNode, context);
                    taskMap.put(nodeExcuteTask, threadPool.submit(nodeExcuteTask));
                }

                for (Map.Entry<NodeExcuteTask, Future> futureEntry : taskMap.entrySet()) {
                    NodeExcuteTask nodeExcuteTask = futureEntry.getKey();
                    Future future = futureEntry.getValue();
                    IFlowNodeInterface detailNode = nodeExcuteTask.getFlowNodeInterface();
                    int timeout = confMap.get(detailNode.getClass()).getTimeout();

                    try {
                        Object result = future.get(timeout, TimeUnit.MILLISECONDS);
                        context.getAdaptorMap().put(detailNode.resultKey(), result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        isThrow = true;
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                        isThrow = true;
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                        isThrow = true;
                    }
                }
            }
            if (isThrow) {
                throw new RuntimeException();
            }
        }


    }

}
