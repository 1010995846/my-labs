package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.Strategy;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

@Strategy
@StrategyMaster
public class RootFacade {

    public String get(){
        return "root";
    }

}
