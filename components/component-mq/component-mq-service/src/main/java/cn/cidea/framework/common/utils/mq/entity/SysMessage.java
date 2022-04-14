package cn.cidea.framework.common.utils.mq.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消息元数据
 * @author Charlotte
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMessage extends Model<SysMessage> {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 消息状态：0-待发送/已接收；1-已发送/已执行
     */
    private String state;

    /**
     * 消息出入：0-发送端；1-接收端
     * 订阅模式下记录各个端点的执行情况
     */
    private String output;

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private String port;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 重试次数
     */
    private int retry;

    /**
     * 异常信息
     */
    private String errorMsg;

    @TableField(exist = false)
    private SysMessageContext context;
}
