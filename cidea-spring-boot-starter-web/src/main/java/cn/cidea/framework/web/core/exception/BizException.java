package cn.cidea.framework.web.core.exception;

import cn.cidea.framework.web.core.asserts.Assert;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Charlotte
 */
public class BizException extends RuntimeException implements IBaseException, Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code;

    public BizException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
    public BizException(String msg) {
        super(msg);
        code = Assert.BIZ.getCode();
    }

    public static BizException error(IBaseException exception){
        return new BizException(exception.getCode(), exception.getMessage());
    }

    public static BizException error(IBaseException exception, String... args) {
        // StringBuilder message = new StringBuilder(exception.getMessage());
        StringBuilder message = new StringBuilder();
        if (args != null && args.length != 0) {
            // message.append(": ");
            message.append(StringUtils.join(args, "; "));
        }
        return new BizException(exception.getCode(), message.toString());
    }
}
