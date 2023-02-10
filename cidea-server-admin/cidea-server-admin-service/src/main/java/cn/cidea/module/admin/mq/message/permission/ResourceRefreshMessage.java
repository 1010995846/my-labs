package cn.cidea.module.admin.mq.message.permission;

import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.client.ChannelName;

/**
 * 菜单数据刷新 Message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResourceRefreshMessage extends AbstractMessage {

    @Override
    public ChannelName getChannel() {
        return new ChannelName("sys.resource.refresh");
    }

}
