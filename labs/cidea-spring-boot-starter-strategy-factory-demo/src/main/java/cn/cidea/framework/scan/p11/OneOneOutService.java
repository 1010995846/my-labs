package cn.cidea.framework.scan.p11;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.scan.p1.OneOutService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p1")
public class OneOneOutService extends OneOutService {
    @Override
    public void print() {
        log.info("OneOneOutService");
    }

}
