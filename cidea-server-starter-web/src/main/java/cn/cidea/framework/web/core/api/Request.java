package cn.cidea.framework.web.core.api;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Charlotte
 */
@Data
public class Request<T> implements Serializable {

    private T data;
}
