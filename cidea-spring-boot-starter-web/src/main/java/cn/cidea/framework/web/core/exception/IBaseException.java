package cn.cidea.framework.web.core.exception;

/**
 * @author Charlotte
 */
public interface IBaseException {

    /**
     * 异常代码
     * @return
     */
    Integer getCode();

    /**
     * 异常信息
     * @return
     */
    String getMessage();

}
