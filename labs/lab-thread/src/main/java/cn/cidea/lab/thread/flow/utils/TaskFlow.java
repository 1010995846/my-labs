package cn.cidea.lab.thread.flow.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * 任务链
 * @author Charlotte
 */
public class TaskFlow {

    @Getter
    private Map<String, Map<Class<? extends IFlowNodeInterface>, NodeConf>> nodeMap = new HashMap<>();

    public NodeConf get(String groupName, Class<? extends IFlowNodeInterface> nodeClass) {
        Map<Class<? extends IFlowNodeInterface>, NodeConf> confMap = nodeMap.get(groupName);
        if(confMap == null){
            return null;
        }
        return confMap.get(nodeClass);
    }

    public void add(Class<? extends IFlowNodeInterface> clazz, NodeConf nodeConf) {
        add(clazz.getName(), clazz, nodeConf);
    }
    public void add(String groupName, Class<? extends IFlowNodeInterface> clazz, NodeConf conf) {
        Map<Class<? extends IFlowNodeInterface>, NodeConf> confMap = nodeMap.get(groupName);
        if(confMap == null){
            confMap = new HashMap<>();
            nodeMap.put(groupName, confMap);
        }
        confMap.put(clazz, conf);
    }

    public void remove(String groupName, Class clazz) {
        String key = clazz.getName();
        if (StringUtils.isNotBlank(groupName)) {
            key = groupName + "_" + key;
        }
        nodeMap.remove(key);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NodeConf {
        private int timeout = 100;
    }

}
