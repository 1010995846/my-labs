package cn.cidea.framework.mq.redisson.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

/**
 * @author Charlotte
 */
@Data
@ConfigurationProperties(prefix = "cidea.mq")
public class RedisMqProperties {

    /**
     * 重试次数对应的间隔
     * 为空或超出次数时不再重试
     */
    private Duration[] retryInterval;
}
