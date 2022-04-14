package cn.cidea.lab.thread.flow.utils;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Context {

    Map<String, Object> adaptorMap = new HashMap<>();

}
