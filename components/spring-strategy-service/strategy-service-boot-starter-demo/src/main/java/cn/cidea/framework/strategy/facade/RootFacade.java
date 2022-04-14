package cn.cidea.framework.strategy.facade;

import cn.cidea.framework.strategy.annotation.StrategyMaster;

@StrategyMaster
public class RootFacade {

    public String get(){
        return "root";
    }

}
