package com.charlotte.core.service.enums;


import com.charlotte.core.service.exception.BusinessException;

public enum BusinessExceptionEnum implements IBaseException, Assert {

    SUCCESS("0000", "成功"),
    SERVER_ERROR("0201", "服务器错误"),
    SYSTEM_ERROR("0400", "系统错误"),
    PARAM_ERROR("0601", "参数错误"),
    VALID_ERROR("0602", "校验失败"),
    UNKNOW_ERROR("9999", "未知错误"),
    ;

    private String code;

    private String msg;

    BusinessExceptionEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public BusinessException newException(String... args) {
        return BusinessException.error(this, args);
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
