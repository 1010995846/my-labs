package cn.cidea.framework.common.utils.mq.service.impl.mq;

import cn.cidea.framework.common.utils.mq.service.IMqService;
import cn.cidea.framework.common.utils.mq.dto.SysMessageDTO;
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
    public void sendMq(SysMessageDTO sysMessage) {
        log.info("active发送完毕");
    }

}
