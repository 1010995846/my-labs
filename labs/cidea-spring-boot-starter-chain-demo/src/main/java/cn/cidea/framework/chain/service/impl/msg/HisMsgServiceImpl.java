package cn.cidea.framework.chain.service.impl.msg;

import cn.cidea.framework.chain.service.IMsgChainService;
import cn.cidea.framework.chain.chain.IChain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Slf4j
@Service
public class HisMsgServiceImpl implements IMsgChainService, IChain {

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void alter() {
        log.info("推送: HIS");
    }
}
