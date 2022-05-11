package cn.cidea.server.dataobject.entity;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.Date;

/**
 * (SysMessage)表实体类
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
public class SysMessage extends Model<SysMessage> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    private String channel;

    /**
     *
     */
    private Long msgId;

    /**
     * 消息内容【json格式】
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
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

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long nextRetry;

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

    private Date createTime;

    private Date updateTime;

}
