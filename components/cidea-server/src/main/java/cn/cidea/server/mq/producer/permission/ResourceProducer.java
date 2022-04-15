package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.server.mq.message.permission.ResourceRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Menu 菜单相关消息的 Producer
 */
@Component
public class ResourceProducer {

    @Resource
    private MQTemplate mqTemplate;

    /**
     * 发送 {@link ResourceRefreshMessage} 消息
     */
    public void sendRefreshMessage() {
        ResourceRefreshMessage message = new ResourceRefreshMessage();
        mqTemplate.send(message);
    }

}
