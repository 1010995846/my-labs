package cn.cidea.framework.mq.redis.core.message;

import lombok.Data;
import lombok.Getter;
import org.redisson.client.protocol.pubsub.Message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类
 * @author Charlotte
 */
@Data
public abstract class AbstractMessage implements Message, Serializable {

    private Long id;
    /**
     * 当前重试次数
     */
    private Integer retry;

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

    /**
     * 获取下一次的重试次数
     * @return
     */
    public Integer nextRetry(){
        if(retry == null){
            retry = 0;
        }
        return ++retry;
    }

    /**
     * 是否进行重试
     * 小于0时不进行自动重试
     * @return
     */
    public boolean retry(){
        return retry == null || retry >= 0;
    }
}
