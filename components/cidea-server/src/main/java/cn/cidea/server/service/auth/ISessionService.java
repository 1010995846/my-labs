package cn.cidea.server.service.auth;

import cn.cidea.server.dataobject.dto.LoginUserDTO;

import java.time.Duration;

/**
 * @author Charlotte
 */
public interface ISessionService {

    void refresh(String sessionId, LoginUserDTO loginDTO);

    String create(LoginUserDTO loginDTO);

    void delete(String sessionId);

    LoginUserDTO get(String sessionId);

    Duration getTimeout();

}
