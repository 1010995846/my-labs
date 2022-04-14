package cn.cidea.framework.web.core.exception;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @author Charlotte
 */
public class BusinessException extends RuntimeException implements IBaseException, Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    private Integer code;

    private BusinessException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }

    public static BusinessException error(IBaseException exception){
        return new BusinessException(exception.getCode(), exception.getMessage());
    }

    public static BusinessException error(IBaseException exception, String... args) {
        // StringBuilder message = new StringBuilder(exception.getMessage());
        StringBuilder message = new StringBuilder();
        if (args != null && args.length != 0) {
            // message.append(": ");
            message.append(StringUtils.join(args, "; "));
        }
        return new BusinessException(exception.getCode(), message.toString());
    }
}
