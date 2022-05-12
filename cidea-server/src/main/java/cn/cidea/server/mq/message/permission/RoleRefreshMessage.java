package cn.cidea.server.mq.message.permission;

import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.client.ChannelName;

/**
 * 角色数据刷新 Message
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleRefreshMessage extends AbstractMessage {

    @Override
    public ChannelName getChannel() {
        return new ChannelName("sys.role.refresh");
    }

}
