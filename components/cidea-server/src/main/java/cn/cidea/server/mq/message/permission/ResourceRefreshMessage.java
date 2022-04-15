package cn.cidea.server.mq.message.permission;

import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 菜单数据刷新 Message
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "sys.resource.refresh";
    }

}
