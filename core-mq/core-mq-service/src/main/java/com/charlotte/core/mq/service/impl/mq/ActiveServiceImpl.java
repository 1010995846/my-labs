package com.charlotte.core.mq.service.impl.mq;

import com.charlotte.core.mq.service.IMqService;
import com.charlotte.core.mq.dto.SysMessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * // TODO Charlotte: 2021/3/9 activemq待集成
 * @author Charlotte
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "spring.activemq", name = "host")
public class ActiveServiceImpl implements IMqService {

    @Override
    public void sendMq(SysMessageDto sysMessage) {
        log.info("active发送完毕");
    }

}
