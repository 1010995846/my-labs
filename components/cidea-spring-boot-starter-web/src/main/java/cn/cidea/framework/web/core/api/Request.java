package cn.cidea.framework.web.core.api;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.framework.web.core.exception.BusinessException;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Charlotte
 */
@Data
public class Request<T> implements Serializable {

    private T data;
}
