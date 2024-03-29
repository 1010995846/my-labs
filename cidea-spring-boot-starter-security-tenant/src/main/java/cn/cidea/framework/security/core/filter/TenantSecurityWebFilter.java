package cn.cidea.framework.security.core.filter;

import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.context.TenantContextHolder;
import cn.cidea.framework.security.core.properties.TenantProperties;
import cn.cidea.framework.security.core.service.TenantFrameworkService;
import cn.cidea.framework.security.core.utils.SecurityFrameworkUtils;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.handler.ExceptionDispatcher;
import cn.cidea.framework.web.core.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 多租户 Security Web 过滤器
 * 1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。
 * 2. 如果请求未带租户的编号，检查是否是忽略的 URL，否则也不允许访问。
 * 3. 校验租户是合法，例如说被禁用、到期
 *
 * 校验用户访问的租户，是否是其所在的租户，
 */
@Slf4j
public class TenantSecurityWebFilter extends OncePerRequestFilter {

    @Autowired
    private TenantProperties tenantProperties;
    @Autowired
    private PathMatcher pathMatcher;
    @Autowired
    private ExceptionDispatcher exceptionDispatcher;
    @Autowired
    private TenantFrameworkService tenantFrameworkService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        Long tenantId = TenantContextHolder.getTenantId();
        // 1. 登陆的用户，校验是否有权限访问该租户，避免越权问题。
        LoginUserDTO user = SecurityFrameworkUtils.getLoginUser();
        if (user != null) {
            // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
            if (tenantId == null) {
                tenantId = user.getTenantId();
                TenantContextHolder.setTenantId(tenantId);
                // 如果传递了租户编号，则进行比对租户编号，避免越权问题
            } else if (!Objects.equals(user.getTenantId(), TenantContextHolder.getTenantId())) {
                log.error("[doFilterInternal][租户({}) User({}/{}) 越权访问租户({}) URL({}/{})]",
                        user.getTenantId(), user.getId(), user.getUserType(),
                        TenantContextHolder.getTenantId(), request.getRequestURI(), request.getMethod());
                ServletUtils.writeJSON(response, Response.fail(Assert.FORBIDDEN.getCode(),
                        "您无权访问该租户的数据"));
                return;
            }
        }

        if (isTargetUrl(request)) {
            // 如果请求未带租户的编号，不允许访问。
            if (tenantId == null) {
                log.error("[doFilterInternal][URL({}/{}) 未传递租户编号]", request.getRequestURI(), request.getMethod());
                ServletUtils.writeJSON(response, Response.fail(Assert.BAD_REQUEST.getCode(),
                        "租户的请求未传递，请进行排查"));
                return;
            }
            // 校验租户是合法，例如说被禁用、到期
            try {
                tenantFrameworkService.validTenant(tenantId);
            } catch (Throwable ex) {
                Response<?> result = exceptionDispatcher.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }

        // 继续过滤
        chain.doFilter(request, response);
    }

    private boolean isTargetUrl(HttpServletRequest request) {
        // 快速匹配，保证性能
        // if (CollUtil.contains(tenantProperties.getTargetUrls(), request.getRequestURI())) {
        //     return true;
        // }
        // 逐个 Ant 路径匹配
        for (String url : CollectionUtils.emptyIfNull(tenantProperties.getTargetUrls())) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

}
