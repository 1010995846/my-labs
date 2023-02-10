package cn.cidea.module.admin.mq.message.test;

import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.client.ChannelName;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestRedisMessage extends AbstractMessage {

    @Override
    public ChannelName getChannel() {
        return new ChannelName("test");
    }

}
