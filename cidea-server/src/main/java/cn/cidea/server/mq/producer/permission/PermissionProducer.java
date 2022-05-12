package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.server.mq.message.permission.RoleResourceRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Permission 权限相关消息的 Producer
 */
@Component
public class PermissionProducer {

    @Resource
    private RedisMQTemplate mqTemplate;

    /**
     * 发送 {@link RoleResourceRefreshMessage} 消息
     */
    public void sendRoleMenuRefreshMessage() {
        RoleResourceRefreshMessage message = new RoleResourceRefreshMessage();
        mqTemplate.pub(message);
    }

}
