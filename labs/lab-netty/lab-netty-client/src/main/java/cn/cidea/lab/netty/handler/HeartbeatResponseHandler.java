package cn.cidea.lab.netty.handler;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.heartbeat.HeartbeatRequest;
import cn.cidea.lab.netty.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Component
public class HeartbeatResponseHandler implements MessageHandler<HeartbeatResponse> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, HeartbeatResponse response) {
        log.info("[execute][收到连接({}) 的心跳响应]id = {}", channel.id(), response.getCode());
        HeartbeatRequest request = new HeartbeatRequest();
        request.setAck(true);
        channel.writeAndFlush(new Invocation(request))
                .addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
    }

    @Override
    public String getType() {
        return HeartbeatResponse.TYPE;
    }
}
