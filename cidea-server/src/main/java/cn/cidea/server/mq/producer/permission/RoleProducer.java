package cn.cidea.server.mq.producer.permission;

import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.server.mq.message.permission.RoleRefreshMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Role 角色相关消息的 Producer
 */
@Component
public class RoleProducer {

    @Resource
    private RedisMQTemplate mqTemplate;

    /**
     * 发送 {@link RoleRefreshMessage} 消息
     */
    public void sendRefreshMessage() {
        RoleRefreshMessage message = new RoleRefreshMessage();
        mqTemplate.pub(message);
    }

}
