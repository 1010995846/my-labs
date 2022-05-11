package cn.cidea.framework.chain.service.impl.msg;

import cn.cidea.framework.chain.chain.IChain;
import cn.cidea.framework.chain.service.IMsgChainService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Charlotte
 */
@Slf4j
public class DefaultMsgServiceImpl implements IMsgChainService, IChain {

    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void alter() {
        log.info("推送: DEFAULT");
    }
}
