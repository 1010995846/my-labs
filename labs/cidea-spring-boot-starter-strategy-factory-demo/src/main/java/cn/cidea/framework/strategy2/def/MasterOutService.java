package cn.cidea.framework.strategy2.def;

import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.strategy2.IOutService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyMaster
public class MasterOutService implements IOutService {
    @Override
    public void print() {
        log.info("MasterOutService");
    }
}
