package cn.cidea.lab.netty.message.chat;

import cn.cidea.lab.netty.message.Request;

import java.util.List;

/**
 * @author Charlotte
 */
public class ChatGroupCreateRequest extends Request {

    public static final String TYPE = "CHAT_GROUP_CREATE_REQUEST";

    /**
     * 房间ID
     */
    private Long groupId;

    /**
     * 加入群聊的用户
     */
    private List<String> userList;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
