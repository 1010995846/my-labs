package cn.cidea.lab.netty.message.heartbeat;


import cn.cidea.lab.netty.core.Message;

/**
 * 消息 - 心跳请求
 */
public class HeartbeatRequest implements Message {

    /**
     * 类型 - 心跳请求
     */
    public static final String TYPE = "HEARTBEAT_REQUEST";

    private Boolean ack;

    public Boolean getAck() {
        return ack;
    }

    public void setAck(Boolean ack) {
        this.ack = ack;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
