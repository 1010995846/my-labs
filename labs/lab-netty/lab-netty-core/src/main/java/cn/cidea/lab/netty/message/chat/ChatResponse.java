package cn.cidea.lab.netty.message.chat;


import cn.cidea.lab.netty.core.Message;
import cn.cidea.lab.netty.message.Response;

/**
 * 消息 - 认证响应
 */
public class ChatResponse extends Response {

    public static final String TYPE = "CHAT_RESPONSE";

    @Override
    public String getType() {
        return TYPE;
    }
}
