package cn.cidea.server.mq.message.test;

import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.redisson.client.ChannelName;

@Data
@EqualsAndHashCode(callSuper = true)
public class TestRedisRetryMessage extends AbstractMessage {

    @Override
    public ChannelName getChannel() {
        return new ChannelName("test_retry");
    }

}
