package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.redis.core.RedisMQTemplate;
import cn.cidea.server.mq.message.permission.ResourceRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Menu 菜单相关消息的 Producer
 */
@Component
public class ResourceProducer {

    @Resource
    private RedisMQTemplate mqTemplate;

    /**
     * 发送 {@link ResourceRefreshMessage} 消息
     */
    public void sendRefreshMessage() {
        ResourceRefreshMessage message = new ResourceRefreshMessage();
        mqTemplate.pub(message);
    }

}
