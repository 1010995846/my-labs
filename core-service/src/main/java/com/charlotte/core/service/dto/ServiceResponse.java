package com.charlotte.core.service.dto;

import com.charlotte.core.service.enums.BusinessExceptionEnum;
import com.charlotte.core.service.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Charlotte
 */
public class ServiceResponse<T> {

    private String code;
    private String msg;
    private T data;

    private ServiceResponse(String code, String... msg) {
        this(null, code, msg);
    }

    private ServiceResponse(T data, String code, String... msg) {
        this.data = data;
        this.code = code;
        this.msg = StringUtils.join(msg, ";");
    }

    public static <T> ServiceResponse<T> success(T data){
        return new ServiceResponse(data, BusinessExceptionEnum.SUCCESS.getCode(), null);
    }

    public static ServiceResponse fail(BusinessException e) {
        return new ServiceResponse(e.getCode(), e.getMessage());
    }

    public static ServiceResponse fail(BusinessExceptionEnum error) {
        return new ServiceResponse(error.getCode(), error.getMsg());
    }

    public static ServiceResponse fail(BusinessExceptionEnum serverError, String... msg) {
        return new ServiceResponse(serverError.getCode(), serverError.getMsg(), msg);
    }

    public static ServiceResponse fail(String code, String msg) {
        return new ServiceResponse(code, msg);
    }
}
