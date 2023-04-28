package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dataobject.dto.MessageDTO;

/**
 * @author Charlotte
 */
public interface IMsgService {

    void ack(MessageDTO messageDto);

}
