package cn.cidea.framework.security.core.service;

import cn.cidea.framework.security.core.LoginUserDTO;

import javax.validation.constraints.NotBlank;
import java.time.Duration;

/**
 * @author Charlotte
 * session操作相关
 */
public interface ISecuritySessionService {

    LoginUserDTO login(@NotBlank String username, @NotBlank String password, String code, String uuid);

    /**
     * 刷新
     * @param sessionId
     * @param loginDTO
     */
    void refresh(String sessionId, LoginUserDTO loginDTO);

    /**
     * 创建
     * @param loginDTO
     * @return
     */
    String create(LoginUserDTO loginDTO);

    /**
     * 删除
     * @param sessionId
     */
    void delete(String sessionId);

    /**
     * 获取
     * @param sessionId
     * @return
     */
    LoginUserDTO get(String sessionId);

    /**
     * 认证并刷新
     * @param sessionId
     * @return
     */
    LoginUserDTO verifySessionAndRefresh(String sessionId);

    /**
     * 测试用模拟session
     * @param userId
     * @return
     */
    LoginUserDTO mock(Long userId);
}
