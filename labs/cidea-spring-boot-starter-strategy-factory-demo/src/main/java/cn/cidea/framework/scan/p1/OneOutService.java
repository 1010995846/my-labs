package cn.cidea.framework.scan.p1;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.scan.IScanService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p1")
public class OneOutService implements IScanService {
    @Override
    public void print() {
        log.info("OneOutService");
    }
}
