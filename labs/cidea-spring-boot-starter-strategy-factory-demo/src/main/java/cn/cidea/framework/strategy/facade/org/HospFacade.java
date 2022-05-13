package cn.cidea.framework.strategy.facade.org;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.facade.OrgFacade;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@StrategyBranch("hosp")
public class HospFacade extends OrgFacade {

    @Override
    public void print() {
        System.out.println("hosp print.");
    }
}
