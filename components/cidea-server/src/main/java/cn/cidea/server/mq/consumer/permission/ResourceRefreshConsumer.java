package cn.cidea.server.mq.consumer.permission;

import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessageListener;
import cn.cidea.server.mq.message.permission.ResourceRefreshMessage;
import cn.cidea.server.service.system.ISysResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 针对 {@link ResourceRefreshMessage} 的消费者
 *
 * @author 芋道源码
 */
@Component
@Slf4j
public class ResourceRefreshConsumer extends AbstractChannelMessageListener<ResourceRefreshMessage> {

    @Resource
    private ISysResourceService resourceService;

    @Override
    public void onMessage(ResourceRefreshMessage message) {
        log.info("[onMessage][收到 Menu 刷新消息]");
        resourceService.initLocalCache();
    }

}
