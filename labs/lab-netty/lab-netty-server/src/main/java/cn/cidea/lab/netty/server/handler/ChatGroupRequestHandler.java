package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.codec.Invocation;
import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.chat.ChatGroupRequest;
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
public class ChatGroupRequestHandler implements MessageHandler<ChatGroupRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyChannelRegistrar registrar;

    @Override
    public void execute(Channel channel, ChatGroupRequest request) {
        registrar.sendToGroup(request.getGroupId(), request.getMessage());
    }

    @Override
    public String getType() {
        return ChatGroupRequest.TYPE;
    }
}
