package cn.cidea.framework.common.utils.mq.service;

import cn.cidea.framework.common.utils.mq.dto.SysMessageDto;

/**
 * @author Charlotte
 */
public interface IMsgService {

    void ack(SysMessageDto messageDto);

}
