package com.charlotte.core.service.entity;

import com.charlotte.core.service.enums.BusinessExceptionEnum;
import com.charlotte.core.service.exception.BusinessException;

public class ServiceResponse<T> {

    private String code;
    private String msg;
    private T data;

    private ServiceResponse(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private ServiceResponse(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ServiceResponse fail(BusinessException e) {
        return new ServiceResponse(e.getCode(), e.getMessage());
    }

    public static ServiceResponse fail(BusinessExceptionEnum serverError) {
        return new ServiceResponse(serverError.getCode(), serverError.getMsg());
    }

    public static ServiceResponse fail(String code, String msg) {
        return new ServiceResponse(code, msg);
    }
}
