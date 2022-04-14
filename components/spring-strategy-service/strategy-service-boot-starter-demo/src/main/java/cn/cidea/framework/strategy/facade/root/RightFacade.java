package cn.cidea.framework.strategy.facade.root;

import cn.cidea.framework.strategy.facade.RootFacade;
import cn.cidea.framework.strategy.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("right")
public class RightFacade extends RootFacade {

    @Override
    public String get() {
        return "right";
    }
}
