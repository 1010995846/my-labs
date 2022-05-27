package cn.cidea.lab.netty.message.chat;


import cn.cidea.lab.netty.message.Response;

/**
 * 消息 - 认证响应
 */
public class ChatRedirectResponse extends Response {

    public static final String TYPE = "CHAT_REDIRECT_RESPONSE";

    @Override
    public String getType() {
        return TYPE;
    }
}
