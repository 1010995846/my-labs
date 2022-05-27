package cn.cidea.lab.netty.message;

import cn.cidea.lab.netty.core.Message;

/**
 * @author Charlotte
 */
public abstract class Request implements Message {

    private Long msgId;

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }
}
