package cn.cidea.framework.chain.service.impl.msg;

import cn.cidea.framework.chain.service.IMsgChainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Slf4j
@Service
public class IhpMsgServiceImpl implements IMsgChainService {

    @Override
    public void alter() {
        log.info("推送: IHP");
    }
}
