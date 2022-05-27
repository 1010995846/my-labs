package cn.cidea.lab.netty.message.auth;


import cn.cidea.lab.netty.core.Message;
import cn.cidea.lab.netty.message.Request;

/**
 * 消息 - 认证请求
 */
public class AuthRequest extends Request {

    public static final String TYPE = "AUTH_REQUEST";

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
