package cn.cidea.server.mq.message.permission;

import cn.cidea.framework.mq.redis.pubsub.AbstractChannelMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色与菜单数据刷新 Message
 *
 * @author 芋道源码
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleResourceRefreshMessage extends AbstractChannelMessage {

    @Override
    public String getChannel() {
        return "sys.role-resource.refresh";
    }

}
