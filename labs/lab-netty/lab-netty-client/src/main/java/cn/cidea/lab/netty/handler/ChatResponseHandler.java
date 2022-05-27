package cn.cidea.lab.netty.handler;

import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.chat.ChatResponse;
import cn.cidea.lab.netty.service.ChatMockService;
import io.netty.channel.Channel;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatResponseHandler implements MessageHandler<ChatResponse> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ChatMockService service;

    @Override
    public void execute(Channel channel, ChatResponse response) {
        log.info("[execute][收到连接({}) 的聊天消息]", channel.id());
        service.ack(response);
    }

    @Override
    public String getType() {
        return ChatResponse.TYPE;
    }
}
