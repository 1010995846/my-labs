package cn.cidea.lab.netty.handler;

import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.chat.ChatRedirectRequest;
import cn.cidea.lab.netty.message.chat.ChatResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatRedirectRequestHandler implements MessageHandler<ChatRedirectRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, ChatRedirectRequest request) {
        log.info("[execute][收到({}) 的聊天消息]", channel.id());
        log.warn("[聊天][{}][接收]{}", request.getFromUser(), request.getMessage());
    }

    @Override
    public String getType() {
        return ChatRedirectRequest.TYPE;
    }
}
