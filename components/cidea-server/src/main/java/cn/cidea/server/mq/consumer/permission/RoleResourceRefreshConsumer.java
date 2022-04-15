package cn.cidea.server.mq.consumer.permission;

import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessageListener;
import cn.cidea.server.mq.message.permission.RoleResourceRefreshMessage;
import cn.cidea.server.service.system.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link RoleResourceRefreshMessage} 的消费者
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class RoleResourceRefreshConsumer extends AbstractChannelMessageListener<RoleResourceRefreshMessage> {

    @Resource
    private ISysPermissionService permissionService;

    @Override
    public void onMessage(RoleResourceRefreshMessage message) {
        log.info("[onMessage][收到 Role 与 Menu 的关联刷新消息]");
        permissionService.initLocalCache();
    }

}
