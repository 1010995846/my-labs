package cn.cidea.module.admin.mq.consumer.permission;

import cn.cidea.framework.mq.redisson.core.message.pubsub.AbstractPubSubListener;
import cn.cidea.module.admin.mq.message.permission.RoleResourceRefreshMessage;
import cn.cidea.module.admin.service.IPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link RoleResourceRefreshMessage} 的消费者
 */
@Component
@Slf4j
public class RoleResourceRefreshConsumer extends AbstractPubSubListener<RoleResourceRefreshMessage> {

    @Resource
    private IPermissionService permissionService;

    @Override
    public void consume(RoleResourceRefreshMessage message) {
        log.info("[onMessage][收到 Role 与 Menu 的关联刷新消息]");
        permissionService.initLocalCache();
    }

}
