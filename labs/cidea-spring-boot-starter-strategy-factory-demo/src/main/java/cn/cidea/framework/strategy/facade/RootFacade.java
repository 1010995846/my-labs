package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.StrategyAPI;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

@StrategyAPI
@StrategyMaster
public class RootFacade {

    public String get(){
        return "root";
    }

}
