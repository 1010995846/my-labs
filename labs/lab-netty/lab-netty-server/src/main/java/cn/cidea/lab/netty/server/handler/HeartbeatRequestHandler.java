package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.heartbeat.HeartbeatRequest;
import cn.cidea.lab.netty.message.heartbeat.HeartbeatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HeartbeatRequestHandler implements MessageHandler<HeartbeatRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, HeartbeatRequest request) {
        if (request.getAck() != null) {
            // 收到二次响应，确认server发送正常，client接收正常
            log.info("[execute][收到连接({}) 的心跳确认响应]id = {}", channel.id());
            return;
        }
        // 确认client发送正常，server接收正常
        log.info("[execute][收到连接({}) 的心跳请求]", channel.id());
        // 响应心跳
        HeartbeatResponse response = new HeartbeatResponse();
        channel.writeAndFlush(new Invocation(response));
    }

    @Override
    public String getType() {
        return HeartbeatRequest.TYPE;
    }
}
