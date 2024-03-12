package cn.cidea.framework.strategy2.p1;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.strategy2.IOutService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p1")
public class OneOutService implements IOutService {
    @Override
    public void print() {
        log.info("OneOutService");
    }
}
