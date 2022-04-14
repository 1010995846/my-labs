package cn.cidea.framework.common.utils.mq.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author Charlotte
 */
@Data
@Accessors(chain = true)
public class SysMessageDto implements Serializable {

    private Long msgId;

    private String context;

}
