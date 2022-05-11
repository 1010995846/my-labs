package cn.cidea.framework.mq.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类
 */
public abstract class AbstractMessage implements Serializable {

    /**
     * 获得 Redis Channel
     *
     * @return Channel
     */
    @JsonIgnore // 避免序列化。原因是，Redis 发布 Channel 消息的时候，已经会指定。
    public abstract String getChannel();

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
