package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.server.mq.message.permission.RoleRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Role 角色相关消息的 Producer
 *
 * @author 芋道源码
 */
@Component
public class RoleProducer {

    @Resource
    private MQTemplate mqTemplate;

    /**
     * 发送 {@link RoleRefreshMessage} 消息
     */
    public void sendRefreshMessage() {
        RoleRefreshMessage message = new RoleRefreshMessage();
        mqTemplate.send(message);
    }

}
