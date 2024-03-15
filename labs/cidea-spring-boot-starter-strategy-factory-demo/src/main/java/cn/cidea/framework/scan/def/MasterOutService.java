package cn.cidea.framework.scan.def;

import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.scan.IScanService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyMaster
public class MasterOutService implements IScanService {
    @Override
    public void print() {
        log.info("MasterOutService");
    }
}
