package com.charlotte.core.service.exception;

import com.charlotte.core.service.enums.IBaseException;

import java.io.Serializable;

/**
 * @author Charlotte
 */
public class BusinessException extends RuntimeException implements IBaseException, Serializable {

    private static final long serialVersionUID = 1L;

    private String code;
    private String msg;

    private BusinessException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static BusinessException error(IBaseException exception){
        return new BusinessException(exception.getCode(), exception.getMsg());
    }

    public static BusinessException error(IBaseException exception, String[] args) {
        StringBuilder msg = new StringBuilder(exception.getMsg());
        if (args != null) {
            msg.append(": ");
            for (String arg : args) {
                msg.append(arg).append("; ");
            }
            msg.delete(msg.length() - 2, msg.length());
            msg.append(".");
        }
        return new BusinessException(exception.getCode(), msg.toString());
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
