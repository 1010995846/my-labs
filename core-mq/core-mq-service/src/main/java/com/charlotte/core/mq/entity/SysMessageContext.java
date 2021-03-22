package com.charlotte.core.mq.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * 消息内容
 * @author Charlotte
 */
@Data
public class SysMessageContext extends Model<SysMessageContext> {

    @TableId(type = IdType.INPUT)
    private Long msgId;

    private String context;

}
