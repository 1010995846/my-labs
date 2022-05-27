package cn.cidea.lab.netty.message.chat;

import cn.cidea.lab.netty.message.Request;

/**
 * @author Charlotte
 */
public class ChatGroupRequest extends Request {

    public static final String TYPE = "CHAT_GROUP_REQUEST";

    private Long groupId;

    private String message;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
