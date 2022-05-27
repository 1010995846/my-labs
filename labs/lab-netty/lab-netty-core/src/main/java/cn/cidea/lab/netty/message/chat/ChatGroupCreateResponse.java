package cn.cidea.lab.netty.message.chat;


import cn.cidea.lab.netty.message.Response;

/**
 * @author Charlotte
 */
public class ChatGroupCreateResponse extends Response {

    public static final String TYPE = "CHAT_GROUP_CREATE_RESPONSE";

    @Override
    public String getType() {
        return TYPE;
    }
}
