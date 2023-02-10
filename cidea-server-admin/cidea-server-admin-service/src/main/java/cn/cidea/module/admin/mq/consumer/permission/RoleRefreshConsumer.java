package cn.cidea.module.admin.mq.consumer.permission;

import cn.cidea.framework.mq.redisson.core.message.pubsub.AbstractPubSubListener;
import cn.cidea.module.admin.mq.message.permission.RoleRefreshMessage;
import cn.cidea.module.admin.service.system.ISysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link RoleRefreshMessage} 的消费者
 */
@Component
@Slf4j
public class RoleRefreshConsumer extends AbstractPubSubListener<RoleRefreshMessage> {

    @Resource
    private ISysRoleService roleService;

    @Override
    public void consume(RoleRefreshMessage message) {
        log.info("[onMessage][收到 Role 刷新消息]");
        roleService.initLocalCache();
    }

}
