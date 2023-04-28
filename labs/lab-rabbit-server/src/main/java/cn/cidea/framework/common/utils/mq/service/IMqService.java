package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dataobject.dto.MessageDTO;

/**
 * @author Charlotte
 */
public interface IMqService {
    /**
     * 发送mq
     * @param sysMessage
     */
    void sendMq(MessageDTO sysMessage);
}
