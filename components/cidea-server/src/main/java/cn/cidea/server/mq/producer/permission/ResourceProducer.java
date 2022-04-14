package cn.cidea.server.mq.producer.permission;

import org.springframework.stereotype.Component;

/**
 * Menu 菜单相关消息的 Producer
 */
@Component
public class ResourceProducer {

    /**
     * 发送 {@link MenuRefreshMessage} 消息
     */
    public void sendMenuRefreshMessage() {
        // TODO
    }

}
