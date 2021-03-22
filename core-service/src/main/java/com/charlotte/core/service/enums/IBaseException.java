package com.charlotte.core.service.enums;

/**
 * @author Charlotte
 */
public interface IBaseException {

    /**
     * 异常代码
     * @return
     */
    String getCode();

    /**
     * 异常信息
     * @return
     */
    String getMsg();

}
