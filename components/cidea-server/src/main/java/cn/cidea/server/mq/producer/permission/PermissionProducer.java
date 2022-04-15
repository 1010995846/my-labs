package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.core.MQTemplate;
import cn.cidea.server.mq.message.permission.RoleResourceRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Permission 权限相关消息的 Producer
 */
@Component
public class PermissionProducer {

    @Resource
    private MQTemplate mqTemplate;

    /**
     * 发送 {@link RoleResourceRefreshMessage} 消息
     */
    public void sendRoleMenuRefreshMessage() {
        // TODO
        RoleResourceRefreshMessage message = new RoleResourceRefreshMessage();
        mqTemplate.send(message);
    }

}
