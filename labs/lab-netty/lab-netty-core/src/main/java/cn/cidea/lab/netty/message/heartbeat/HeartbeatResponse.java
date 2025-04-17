package cn.cidea.lab.netty.message.heartbeat;


import cn.cidea.lab.netty.message.Response;

/**
 * 消息 - 心跳响应
 */
public class HeartbeatResponse extends Response {

    /**
     * 类型 - 心跳响应
     */
    public static final String TYPE = "HEARTBEAT_RESPONSE";

    @Override
    public String getType() {
        return TYPE;
    }
}
