package cn.cidea.framework.web.core.asserts;


import cn.cidea.framework.web.core.exception.BusinessException;
import cn.cidea.framework.web.core.exception.IBaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常定义
 * @author Charlotte
 */
@AllArgsConstructor
public enum Assert implements IBaseException, IAssert {

    SUCCESS(0, "成功"),
    UNAUTHORIZED(301, "账号未登录"),
    BAD_CREDENTIALS(302, "登录失败"),
    FORBIDDEN(303, "没有该操作权限"),
    BAD_REQUEST(400, "参数异常"),
    SERVER(500, "服务器异常"),
    BUSINESS(600, "业务异常"),
    VALID(0602, "校验失败"),
    UNKNOWN(999, "未知错误"),
    ;

    @Getter
    private Integer code;

    @Getter
    private String message;

    @Override
    public BusinessException build(String... args) {
        return BusinessException.error(this, args);
    }


}
