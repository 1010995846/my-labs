package cn.cidea.framework.security.core.utils;

import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.utils.WebFrameworkUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 安全服务工具类
 */
public class SecurityFrameworkUtils {

    private SecurityFrameworkUtils() {}

    /**
     * 从请求中，获得认证 Token
     *
     * @param request 请求
     * @param header 认证 Token 对应的 Header 名字
     * @return 认证 Token
     */
    public static String obtainAuthorization(HttpServletRequest request, String header) {
        String authorization = request.getHeader(header);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf("Bearer ");
        if (index == -1) {
            return null;
        }
        return authorization.substring(index + 7).trim();
    }

    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static LoginUserDTO getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUserDTO ? (LoginUserDTO) authentication.getPrincipal() : null;
    }

    public static LoginUserDTO getAndValidLoginUser() {
        LoginUserDTO loginUser = getLoginUser();
        Assert.UNAUTHORIZED.nonNull(loginUser);
        return loginUser;
    }

    /**
     * 获得当前用户的编号，从上下文中
     *
     * @return 用户编号
     */
    @Nullable
    public static Long getLoginUserId() {
        LoginUserDTO loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request 请求
     */
    public static void setLoginUser(LoginUserDTO loginUser, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        Authentication authentication = buildAuthentication(loginUser, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 额外设置到 request 中，用于 ApiAccessLogFilter 可以获取到用户编号；
        // 原因是，Spring Security 的 Filter 在 ApiAccessLogFilter 后面，在它记录访问日志时，线上上下文已经没有用户编号等信息
        WebFrameworkUtils.setLoginUserId(request, loginUser.getId());
        WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
    }

    private static Authentication buildAuthentication(LoginUserDTO loginUser, HttpServletRequest request) {
        // 创建 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, loginUser.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

}
