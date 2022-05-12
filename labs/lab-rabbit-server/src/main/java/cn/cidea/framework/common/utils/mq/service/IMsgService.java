package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dto.SysMessageDTO;

/**
 * @author Charlotte
 */
public interface IMsgService {

    void ack(SysMessageDTO messageDto);

}
