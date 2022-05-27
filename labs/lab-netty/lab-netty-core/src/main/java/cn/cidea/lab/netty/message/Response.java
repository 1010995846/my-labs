package cn.cidea.lab.netty.message;

import cn.cidea.lab.netty.core.Message;

/**
 * @author Charlotte
 */
public abstract class Response implements Message {

    public Long msgId;

    public Integer code = 0;

    public String message;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean successful(){
        return code == 0;
    }
}
