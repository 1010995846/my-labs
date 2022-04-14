package cn.cidea.framework.strategy.facade.root;

import cn.cidea.framework.strategy.facade.RootFacade;
import cn.cidea.framework.strategy.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("left")
public class LeftFacade extends RootFacade {

    @Override
    public String get() {
        return "left";
    }
}
