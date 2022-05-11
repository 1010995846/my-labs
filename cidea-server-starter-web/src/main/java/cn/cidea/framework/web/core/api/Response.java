package cn.cidea.framework.web.core.api;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.exception.BusinessException;
import lombok.Data;

import java.io.Serializable;

/**
 * // TODO 后续使用网关过滤Request和Response
 * @author Charlotte
 */
@Data
public class Response<T> implements Serializable {

    private Integer code;

    /**
     * 返回数据
     */
    private T data;
    /**
     * 错误提示，用户可阅读
     */
    private String message;

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setData(data);
        response.setCode(Assert.SUCCESS.getCode());
        return response;
    }


    public static Response fail(Assert exception) {
        Response<Object> response = new Response<>();
        response.setCode(exception.getCode());
        response.setMessage(exception.getMessage());
        return response;
    }

    public static Response fail(Integer code, String message) {
        Response<Object> response = new Response<>();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    public static Response fail(BusinessException e) {
        Response<Object> response = new Response<>();
        response.setCode(e.getCode());
        response.setMessage(e.getMessage());
        return response;
    }
}
