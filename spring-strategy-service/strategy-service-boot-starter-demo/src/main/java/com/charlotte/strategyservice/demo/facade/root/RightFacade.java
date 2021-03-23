package com.charlotte.strategyservice.demo.facade.root;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.facade.RootFacade;

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
