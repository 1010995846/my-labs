package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dto.SysMessageDTO;

/**
 * @author Charlotte
 */
public interface IMqService {
    /**
     * 发送mq
     * @param sysMessage
     */
    void sendMq(SysMessageDTO sysMessage);
}
