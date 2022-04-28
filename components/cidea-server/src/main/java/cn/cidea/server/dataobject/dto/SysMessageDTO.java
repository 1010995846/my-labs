package cn.cidea.server.dataobject.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * (SysMessage)表实体类
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Data
public class SysMessageDTO implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     *
     */
    private Long msgId;
    /**
     * 消息内容【json格式】
     */
    private Object content;
    /**
     * 0-未投递；1-等待ack；2-消费成功；-1；消费失败
     */
    private Integer state;
    /**
     * ack状态
     */
    private Boolean ack;
    /**
     * 重试次数
     */
    private Integer retry;
    /**
     * 异常信息
     */
    private String errorMsg;
    /**
     *
     */
    private String output;
    /**
     *
     */
    private String host;
    /**
     *
     */
    private String port;
    /**
     *
     */
    private String applicationName;
}
