package cn.cidea.framework.common.utils.mq.dataobject.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Charlotte
 */
@Data
@Accessors(chain = true)
public class MessageDTO implements Serializable {

    private Long msgId;

    private String context;

}
