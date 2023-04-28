package cn.cidea.framework.common.utils.mq.dataobject.dto;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * (SysMessage)表实体类
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Data
public class SysMessageDTO implements Serializable {

    private Long id;

    private String channel;

    private Long msgId;
    /**
     * 消息内容【json格式】
     */
    private JSONObject content;

    private String className;
    /**
     * 0-初始；1-消费成功；-1；消费失败
     */
    private Integer status;
    /**
     * ack状态
     */
    private Boolean ack;
    /**
     * 重试次数
     */
    private Integer retry;

    private Long nextRetry;
    /**
     * 异常信息
     */
    private String errorMsg;

    private String output;

    private String host;

    private String port;

    private String applicationName;

    private Date createTime;

    private Date updateTime;
}
