package cn.cidea.module.admin.mq.producer.permission;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.module.admin.mq.message.permission.ResourceRefreshMessage;
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
     * 发送 {@link cn.cidea.admin.mq.message.permission.ResourceRefreshMessage} 消息
     */
    public void sendRefreshMessage() {
        ResourceRefreshMessage message = new ResourceRefreshMessage();
        mqTemplate.pub(message);
    }

}
