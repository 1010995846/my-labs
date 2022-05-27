package cn.cidea.lab.netty.message.auth;


import cn.cidea.lab.netty.core.Message;
import cn.cidea.lab.netty.message.Response;


/**
 * 消息 - 认证响应
 */
public class AuthResponse extends Response {

    public static final String TYPE = "AUTH_RESPONSE";

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
