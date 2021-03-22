package com.charlotte.core.mq.service;

import com.charlotte.core.mq.dto.SysMessageDto;

/**
 * @author Charlotte
 */
public interface IMqService {
    /**
     * 发送mq
     * @param sysMessage
     */
    void sendMq(SysMessageDto sysMessage);
}
