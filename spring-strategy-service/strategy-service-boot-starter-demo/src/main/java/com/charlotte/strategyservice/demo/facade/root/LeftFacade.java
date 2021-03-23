package com.charlotte.strategyservice.demo.facade.root;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.facade.RootFacade;

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
