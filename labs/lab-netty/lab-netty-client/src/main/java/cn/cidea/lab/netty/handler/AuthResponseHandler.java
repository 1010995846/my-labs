package cn.cidea.lab.netty.handler;

import cn.cidea.lab.netty.AuthContext;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.auth.AuthResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuthResponseHandler implements MessageHandler<AuthResponse> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, AuthResponse response) {
        // 响应心跳
        if (response.getCode() == 0) {
            log.info("[execute][收到连接({}) 的认证请求][认证成功]", channel.id());
            AuthContext.setName(response.getToken());
        } else {
            log.error("[execute][收到连接({}) 的认证请求][认证异常]", channel.id());
        }
    }

    @Override
    public String getType() {
        return AuthResponse.TYPE;
    }
}
