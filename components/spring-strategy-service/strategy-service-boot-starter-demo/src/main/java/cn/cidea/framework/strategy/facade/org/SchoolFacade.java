package cn.cidea.framework.strategy.facade.org;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.facade.OrgFacade;

/**
 * @author Charlotte
 */
@StrategyBranch("school")
public class SchoolFacade extends OrgFacade {
    @Override
    public void print() {
        System.out.println("school print.");
    }
}
