package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.auth.AuthRequest;
import cn.cidea.lab.netty.message.auth.AuthResponse;
import cn.cidea.lab.netty.message.chat.ChatRedirectRequest;
import cn.cidea.lab.netty.message.chat.ChatRequest;
import cn.cidea.lab.netty.message.chat.ChatResponse;
import cn.cidea.lab.netty.server.NettyChannelRegistrar;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatRequestHandler implements MessageHandler<ChatRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyChannelRegistrar registrar;

    @Override
    public void execute(Channel channel, ChatRequest request) {
        log.info("[execute][收到连接({}) 的聊天消息]", channel.id());

        ChatRedirectRequest redirectRequest = new ChatRedirectRequest();
        redirectRequest.setFromUser(request.getToken());
        redirectRequest.setMessage(request.getMessage());
        registrar.send(request.getToUser(), redirectRequest);

        ChatResponse response = new ChatResponse();
        response.setMsgId(request.getMsgId());
        channel.writeAndFlush(new Invocation(response));
    }

    @Override
    public String getType() {
        return ChatRequest.TYPE;
    }
}
