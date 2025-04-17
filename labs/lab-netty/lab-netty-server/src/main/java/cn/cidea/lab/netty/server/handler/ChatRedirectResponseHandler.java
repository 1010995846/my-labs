package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.chat.ChatRedirectResponse;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ChatRedirectResponseHandler implements MessageHandler<ChatRedirectResponse> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void execute(Channel channel, ChatRedirectResponse request) {
        log.info("确认收到响应");
    }

    @Override
    public String getType() {
        return ChatRedirectResponse.TYPE;
    }
}
