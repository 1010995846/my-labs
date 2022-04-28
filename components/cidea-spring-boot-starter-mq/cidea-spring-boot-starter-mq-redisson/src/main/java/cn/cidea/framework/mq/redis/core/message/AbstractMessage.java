package cn.cidea.framework.mq.redis.core.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.redisson.client.protocol.pubsub.Message;

import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Redis 消息抽象基类
 * @author Charlotte
 */
@Data
public abstract class AbstractMessage<T> implements Message, Serializable {

    private Long id;
    /**
     * 第n次重试
     * 手动设为小于0时不进行重试
     */
    private Integer retry;

    /**
     * 下次重试间隔，为空时不进行重试
     */
    @JsonIgnore
    private Duration nextDuration;

    private Date sendTime;

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
