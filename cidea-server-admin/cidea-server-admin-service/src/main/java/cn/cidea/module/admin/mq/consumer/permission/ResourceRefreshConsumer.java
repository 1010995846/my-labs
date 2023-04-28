package cn.cidea.module.admin.mq.consumer.permission;

import cn.cidea.framework.mq.redisson.core.message.pubsub.AbstractPubSubListener;
import cn.cidea.module.admin.mq.message.permission.ResourceRefreshMessage;
import cn.cidea.module.admin.service.ISysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link ResourceRefreshMessage} 的消费者
 */
@Component
@Slf4j
public class ResourceRefreshConsumer extends AbstractPubSubListener<ResourceRefreshMessage> {

    @Resource
    private ISysResourceService resourceService;

    @Override
    public void consume(ResourceRefreshMessage message) {
        log.info("[onMessage][收到 Resource 刷新消息]");
        resourceService.initLocalCache();
    }
}
