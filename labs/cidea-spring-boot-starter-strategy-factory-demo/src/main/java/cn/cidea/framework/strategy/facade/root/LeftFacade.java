package cn.cidea.framework.strategy.facade.root;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.facade.RootFacade;

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
