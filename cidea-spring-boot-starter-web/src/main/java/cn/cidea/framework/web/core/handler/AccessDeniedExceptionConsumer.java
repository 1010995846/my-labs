package cn.cidea.framework.web.core.handler;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.framework.web.core.utils.WebFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

import static cn.cidea.framework.web.core.asserts.Assert.FORBIDDEN;

/**
 * @author: CIdea
 */
@Slf4j
@ConditionalOnClass(AccessDeniedException.class)
public class AccessDeniedExceptionConsumer extends ExceptionConsumer<AccessDeniedException> {

    @Override
    protected Class ec() {
        return AccessDeniedException.class;
    }

    /**
     * 处理 Spring Security 权限不足的异常
     * 来源是，使用 @PreAuthorize 注解，AOP 进行权限拦截
     */
    @Override
    @ExceptionHandler(value = AccessDeniedException.class)
    Response<?> handler(HttpServletRequest req, AccessDeniedException ex) {
        log.warn("[accessDeniedExceptionHandler][userId({}) 无法访问 url({})]", WebFrameworkUtils.getLoginUserId(req),
                req.getRequestURL(), ex);
        return Response.fail(FORBIDDEN);
    }
}
