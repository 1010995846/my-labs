package cn.cidea.module.admin.service.auth;

import cn.cidea.framework.security.core.service.ISecurityLoginService;
import cn.cidea.framework.security.core.service.ISecuritySessionService;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.module.admin.dataobject.convert.SysUserConvert;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import cn.cidea.module.admin.dataobject.entity.pool.SysUserPool;
import cn.cidea.module.admin.dataobject.enums.LoginResultEnum;
import cn.cidea.module.admin.dataobject.enums.LoginTypeEnum;
import cn.cidea.module.admin.service.common.ICaptchaService;
import cn.cidea.module.admin.service.system.ISysUserLoginLogService;
import cn.cidea.module.admin.service.system.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class LoginServiceImpl implements ISecurityLoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysUserLoginLogService loginLogService;
    @Autowired
    private ICaptchaService captchaService;
    @Autowired
    private ISecuritySessionService sessionService;

    @Autowired
    private SysUserPool userPool;

    @Override
    public LoginUserDTO loadUserByUsername(String username) {
        SysUser user = userService.getUserByUsername(username);
        Assert.BAD_CREDENTIALS.nonNull(user, "用户名不存在");
        Assert.BAD_CREDENTIALS.isFalse(user.getDisabled(), "账号被禁用");
        Assert.BAD_CREDENTIALS.isFalse(user.getDeleted(), "账号被删除");
        userPool.builder(user).role();
        return SysUserConvert.INSTANCE.toLoginDTO(user);
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


}
