package cn.cidea.framework.strategy2.p2;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy2.IOutService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p2")
public class TwoOutService implements IOutService {
    @Override
    public void print() {
        log.info("TwoOutService");
    }
}
