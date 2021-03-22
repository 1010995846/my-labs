package com.charlotte.core.mq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.charlotte.core.mq.entity.SysMessage;

/**
 * @author Charlotte
 */
public interface IMessageService extends IService<SysMessage> {

    /**
     * 发送消息
     * @param context
     */
    void send(String context);

}
