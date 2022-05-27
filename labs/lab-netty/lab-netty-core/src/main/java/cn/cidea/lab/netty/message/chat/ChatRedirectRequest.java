package cn.cidea.lab.netty.message.chat;

import cn.cidea.lab.netty.message.Request;

/**
 * @author Charlotte
 */
public class ChatRedirectRequest extends Request {

    public static final String TYPE = "CHAT_REDIRECT_REQUEST";

    private String fromUser;

    private String message;

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
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
