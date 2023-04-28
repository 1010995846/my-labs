package cn.cidea.framework.security.core.filter;

import cn.cidea.framework.security.core.properties.SecurityProperties;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.service.ISecuritySessionService;
import cn.cidea.framework.security.core.utils.SecurityFrameworkUtils;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.framework.web.core.handler.ExceptionDispatcher;
import cn.hutool.core.util.StrUtil;
import cn.cidea.framework.web.core.utils.ServletUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUserDTO} 信息，并加入到 Spring Security 上下文
 */
public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private SecurityProperties properties;
    @Autowired
    private ISecuritySessionService sessionService;
    @Autowired
    private ExceptionDispatcher exceptionDispatcher;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String sessionId = SecurityFrameworkUtils.obtainAuthorization(request, properties.getSessionHeader());
        if (StrUtil.isNotEmpty(sessionId)) {
            try {
                // 验证 token 有效性
                LoginUserDTO loginUser = sessionService.verifySessionAndRefresh(sessionId);
                // 模拟 Login 功能，方便日常开发调试
                if (loginUser == null) {
                    loginUser = this.mockLoginUser(request, sessionId);
                }
                // 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                Response<?> result = exceptionDispatcher.defaultExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }

        // 继续过滤链
        chain.doFilter(request, response);
    }

    /**
     * 模拟登录用户，方便日常开发调试
     *
     * 注意，在线上环境下，一定要关闭该功能！！！
     *
     * @param request 请求
     * @param sessionId 模拟的 session，格式为 {@link SecurityProperties#getMockSecret()} + 用户编号
     * @return 模拟的 LoginUser
     */
    private LoginUserDTO mockLoginUser(HttpServletRequest request, String sessionId) {
        if (!properties.getMockEnable()) {
            return null;
        }
        // 必须以 mockSecret 开头
        if (!sessionId.startsWith(properties.getMockSecret())) {
            return null;
        }
        Long userId = Long.valueOf(sessionId.substring(properties.getMockSecret().length()));
        return sessionService.mock(userId);
    }

}
