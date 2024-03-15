package cn.cidea.framework.scan.p2;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.scan.IScanService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p2")
public class TwoOutService implements IScanService {
    @Override
    public void print() {
        log.info("TwoOutService");
    }
}
