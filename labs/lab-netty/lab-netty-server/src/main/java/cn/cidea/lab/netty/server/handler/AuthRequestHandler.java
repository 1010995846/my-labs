package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.auth.AuthRequest;
import cn.cidea.lab.netty.message.auth.AuthResponse;
import cn.cidea.lab.netty.server.NettyChannelRegistrar;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthRequestHandler implements MessageHandler<AuthRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyChannelRegistrar registrar;

    @Override
    public void execute(Channel channel, AuthRequest request) {
        log.info("[execute][收到连接({}) 的认证请求]token = {}", channel.id(), request.getToken());
        // 响应心跳
        AuthResponse response = new AuthResponse();
        if (request.getToken() != null) {
            response.setCode(0);
            response.setToken(request.getToken());
            // 省略认证流程，直接用token作为用户ID和用户名
            registrar.addUser(channel, request.getToken());
        } else {
            log.error("[execute][({}) 认证异常]", channel.id());
            response.setCode(1);
        }
        channel.writeAndFlush(new Invocation(response));
    }

    @Override
    public String getType() {
        return AuthRequest.TYPE;
    }
}
