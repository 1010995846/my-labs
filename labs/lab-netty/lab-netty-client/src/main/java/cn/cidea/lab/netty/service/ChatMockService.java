package cn.cidea.lab.netty.service;

import cn.cidea.lab.netty.AuthContext;
import cn.cidea.lab.netty.client.NettyClient;
import cn.cidea.lab.netty.message.chat.ChatGroupCreateRequest;
import cn.cidea.lab.netty.message.chat.ChatGroupRequest;
import cn.cidea.lab.netty.message.chat.ChatRequest;
import cn.cidea.lab.netty.message.chat.ChatResponse;
import cn.hutool.core.lang.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlotte
 */
@Service
public class ChatMockService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    private NettyClient nettyClient;

    private Snowflake Snowflake = new Snowflake();
    private Map<Long, ChatRequest> cache = new ConcurrentHashMap<>();

    public void sendTo(String toUser, String message) {
        ChatRequest request = new ChatRequest();
        long msgId = Snowflake.nextId();
        request.setMsgId(msgId);
        request.setToken(AuthContext.getAndValidName());
        request.setToUser(toUser);
        request.setMessage(message);
        nettyClient.send(request);
        cache.put(msgId, request);
    }

    public void group(ChatGroupCreateRequest request){
        request.setMsgId(Snowflake.nextId());
        nettyClient.send(request);
    }
    public void sendToGroup(ChatGroupRequest request){
        request.setMsgId(Snowflake.nextId());
        nettyClient.send(request);
    }

    public void ack(ChatResponse response) {
        Long msgId = response.getMsgId();
        ChatRequest request = cache.get(msgId);
        if(response.successful()){
            log.warn("[聊天][{}][发送]{}:{}", request.getToUser(), request.getMessage(), request.getToken());
        } else {
            log.error("[聊天][{}][发送][失败]{}:{}", request.getToUser(), request.getMessage(), request.getToken());
        }
    }
}
