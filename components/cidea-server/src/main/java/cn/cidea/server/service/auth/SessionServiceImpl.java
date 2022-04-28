package cn.cidea.server.service.auth;

import cn.cidea.server.dal.redis.LoginUserRedisDAO;
import cn.cidea.server.framework.security.config.SecurityProperties;
import cn.hutool.core.util.IdUtil;
import cn.cidea.server.dataobject.dto.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;

/**
 * @author Charlotte
 */
@Slf4j
@Component
public class SessionServiceImpl implements ISessionService {

    @Autowired
    private LoginUserRedisDAO loginUserRedisDAO;
    @Autowired
    private SecurityProperties properties;

    private static final String LOGIN_TOKEN_KEY = "login_session:";

    @Override
    public void refresh(String sessionId, LoginUserDTO loginDTO) {
        // 写入 Redis 缓存
        loginUserRedisDAO.set(sessionId, loginDTO);
        Date loginTime = new Date();
        loginDTO.setUpdateTime(loginTime);
        loginDTO.setLoginTime(loginTime);
        // loginDTO.setExpireTime(loginDTO.getLoginTime() + properties.getExpireTimeUnit().toMillis(properties.getExpireTime()));
        loginUserRedisDAO.set(sessionId, loginDTO);
    }

    @Override
    public String create(LoginUserDTO loginDTO) {
        // <1> 设置 LoginUser 的用户唯一标识。注意，这里虽然变量名叫 sessionId ，其实不是身份认证的 Token
        String sessionId = IdUtil.fastUUID();
        refresh(sessionId, loginDTO);
        return sessionId;
    }

    @Override
    public void delete(String sessionId){
        // 删除 Redis 缓存
        loginUserRedisDAO.delete(sessionId);
    }


    @Override
    public LoginUserDTO get(String sessionId) {
        return loginUserRedisDAO.get(sessionId);
    }

    @Override
    public Duration getTimeout() {
        return properties.getSessionTimeout();
    }
}
