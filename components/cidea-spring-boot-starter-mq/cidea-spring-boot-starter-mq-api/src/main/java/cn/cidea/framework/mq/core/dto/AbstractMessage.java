package cn.cidea.framework.mq.core.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类
 */
public abstract class AbstractMessage implements Serializable {

    /**
     * 头
     */
    private Map<String, String> headers = new HashMap<>();

    public String getHeader(String key) {
        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

}
