package cn.cidea.lab.netty.message.chat;

import cn.cidea.lab.netty.core.Message;
import cn.cidea.lab.netty.message.Request;
import cn.cidea.lab.netty.message.auth.AuthRequest;

/**
 * @author Charlotte
 */
public class ChatRequest extends AuthRequest {

    public static final String TYPE = "CHAT_REQUEST";

    private String toUser;

    private String message;

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
