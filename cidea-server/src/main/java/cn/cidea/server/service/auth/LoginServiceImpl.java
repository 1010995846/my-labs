package cn.cidea.server.service.auth;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.server.service.common.ICaptchaService;
import cn.cidea.server.service.system.ISysUserLoginLogService;
import cn.cidea.server.dataobject.covert.SysUserCovert;
import cn.cidea.server.dataobject.dto.LoginUserDTO;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.server.dataobject.entity.pool.SysUserPool;
import cn.cidea.server.dataobject.enums.LoginTypeEnum;
import cn.cidea.server.dataobject.enums.LoginResultEnum;
import cn.cidea.server.service.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
@Slf4j
public class LoginServiceImpl implements ILoginService {

    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserLoginLogService loginLogService;
    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private ISessionService sessionService;

    @Autowired
    private SysUserPool userPool;

    @Override
    public LoginUserDTO loadUserByUsername(String username) {
        SysUser user = userService.getUserByUsername(username);
        Assert.BAD_CREDENTIALS.nonNull(user, "用户名不存在");
        Assert.BAD_CREDENTIALS.isFalse(user.getDisabled(), "账号被禁用");
        Assert.BAD_CREDENTIALS.isFalse(user.getDeleted(), "账号被删除");
        userPool.builder(user).role();
        return SysUserCovert.INSTANCE.toLoginDTO(user);
    }

    @Override
    public LoginUserDTO login(String username, String password, String code, String uuid) {
        captchaService.verify(uuid, code);

        final LoginTypeEnum logTypeEnum = LoginTypeEnum.LOGIN_USERNAME;
        // <2> 用户验证
        Authentication authentication;
        try {
            // 调用 Spring Security 的 AuthenticationManager#authenticate(...) 方法，使用账号密码进行认证
            // 在其内部，会调用到 loadUserByUsername 方法，获取 User 信息
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
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
    public LoginUserDTO verifySessionAndRefresh(String sessionId) {
        // 获得 LoginUser
        LoginUserDTO loginUser = sessionService.get(sessionId);
        if (loginUser == null) {
            return null;
        }
        // 刷新 LoginUser 缓存
        return this.refreshLoginUserCache(sessionId, loginUser);
    }
    
    private LoginUserDTO refreshLoginUserCache(String sessionId, LoginUserDTO loginUser) {
        // 每 1/3 的 Session 超时时间，刷新 LoginUser 缓存
        if (System.currentTimeMillis() - loginUser.getUpdateTime().getTime() <
                sessionService.getTimeout().toMillis() / 3) {
            return loginUser;
        }

        // 重新加载 LoginUser 信息
        loginUser = SysUserCovert.INSTANCE.toLoginDTO(userService.getById(loginUser.getId()));
        if (loginUser == null || !loginUser.isEnabled()) {
            // 校验 sessionId 时，用户被禁用的情况下，也认为 sessionId 过期，方便前端跳转到登录界面
            throw Assert.BAD_CREDENTIALS.build("Session 已经过期");
        }

        // 刷新 LoginUser 缓存
        sessionService.refresh(sessionId, loginUser);
        return loginUser;
    }

    @Override
    public LoginUserDTO mockLogin(Long userId) {
        return SysUserCovert.INSTANCE.toLoginDTO(userService.getById(userId));
    }
}
