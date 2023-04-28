package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.security.core.properties.SecurityProperties;
import cn.cidea.framework.security.core.service.ISecuritySessionService;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.redis.LoginUserRedisDAO;
import cn.cidea.module.admin.dataobject.convert.SysUserConvert;
import cn.cidea.module.admin.dataobject.entity.pool.SysUserPool;
import cn.cidea.module.admin.dataobject.enums.LoginResultEnum;
import cn.cidea.module.admin.dataobject.enums.LoginTypeEnum;
import cn.cidea.module.admin.service.ICaptchaService;
import cn.cidea.module.admin.service.ISysUserLoginLogService;
import cn.cidea.module.admin.service.ISysUserService;
import cn.hutool.core.util.IdUtil;
import cn.cidea.framework.security.core.LoginUserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Charlotte
 */
@Slf4j
@Component
public class SessionServiceImpl implements ISecuritySessionService {

    @Autowired
    @Lazy
    private ISysUserService userService;
    @Autowired
    private LoginUserRedisDAO loginUserRedisDAO;
    @Autowired
    private SecurityProperties properties;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ISysUserLoginLogService loginLogService;
    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private ISecuritySessionService sessionService;


    @Override
    public LoginUserDTO login(String username, String password, String code, String uuid) {
        captchaService.verify(uuid, code);

        final LoginTypeEnum logTypeEnum = LoginTypeEnum.LOGIN_USERNAME;
        // 用户验证
        Authentication authentication;
        try {
            // 调用 Spring Security 的 AuthenticationManager#authenticate(...) 方法，使用账号密码进行认证
            // 在其内部，会调用到 loadUserByUsername 方法，获取 User 信息
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException badCredentialsException) {
            loginLogService.fail(username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS, badCredentialsException);
            throw Assert.BAD_CREDENTIALS.build("账号密码不正确");
        } catch (DisabledException disabledException) {
            loginLogService.fail(username, logTypeEnum, LoginResultEnum.USER_DISABLED, disabledException);
            throw Assert.BAD_CREDENTIALS.build("账号被禁用");
        } catch (AuthenticationException authenticationException) {
            Throwable cause = authenticationException.getCause();
            if(cause == null){
                cause = authenticationException;
            }
            log.error("[登录异常]", cause);
            loginLogService.fail(username, logTypeEnum, LoginResultEnum.UNKNOWN_ERROR, cause);
            throw Assert.BAD_CREDENTIALS.build(cause.getMessage());
        }
        // 登录成功的日志
        Assert.SERVER.nonNull(authentication.getPrincipal(), "Principal 不会为空");
        loginLogService.success(username, logTypeEnum);
        LoginUserDTO principal = (LoginUserDTO) authentication.getPrincipal();
        String sessionId = sessionService.create(principal);
        principal.setSessionId(sessionId);
        return principal;
    }


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
    public LoginUserDTO verifySessionAndRefresh(String sessionId) {
        // 获得 LoginUser
        LoginUserDTO loginUser = get(sessionId);
        if (loginUser == null) {
            return null;
        }
        // 刷新 LoginUser 缓存
        return this.refreshLoginUserCache(sessionId, loginUser);
    }

    private LoginUserDTO refreshLoginUserCache(String sessionId, LoginUserDTO loginUser) {
        // 每 1/3 的 Session 超时时间，刷新 LoginUser 缓存
        if (System.currentTimeMillis() - loginUser.getUpdateTime().getTime() <
                properties.getSessionTimeout().toMillis() / 3) {
            return loginUser;
        }

        // 重新加载 LoginUser 信息
        loginUser = SysUserConvert.INSTANCE.toLoginDTO(userService.getById(loginUser.getId()));
        if (loginUser == null || !loginUser.isEnabled()) {
            // 校验 sessionId 时，用户被禁用的情况下，也认为 sessionId 过期，方便前端跳转到登录界面
            throw Assert.BAD_CREDENTIALS.build("Session 已经过期");
        }

        // 刷新 LoginUser 缓存
        refresh(sessionId, loginUser);
        return loginUser;
    }

    @Override
    public LoginUserDTO mock(Long userId) {
        return SysUserConvert.INSTANCE.toLoginDTO(userService.getById(userId));
    }
}
