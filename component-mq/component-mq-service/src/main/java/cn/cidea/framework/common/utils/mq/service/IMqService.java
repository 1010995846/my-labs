package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dto.SysMessageDto;

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
