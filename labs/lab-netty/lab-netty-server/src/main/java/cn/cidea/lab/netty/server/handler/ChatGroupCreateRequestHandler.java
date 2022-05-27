package cn.cidea.lab.netty.server.handler;

import cn.cidea.lab.netty.core.MessageHandler;
import cn.cidea.lab.netty.message.chat.ChatGroupCreateRequest;
import cn.cidea.lab.netty.server.NettyChannelRegistrar;
import com.alibaba.fastjson.JSONObject;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatGroupCreateRequestHandler implements MessageHandler<ChatGroupCreateRequest> {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NettyChannelRegistrar registrar;

    @Override
    public void execute(Channel channel, ChatGroupCreateRequest request) {
        log.info("创建/编辑群组成员，{}", JSONObject.toJSONString(request));
        registrar.editGroup(request.getGroupId(), request.getUserList());
    }

    @Override
    public String getType() {
        return ChatGroupCreateRequest.TYPE;
    }
}
