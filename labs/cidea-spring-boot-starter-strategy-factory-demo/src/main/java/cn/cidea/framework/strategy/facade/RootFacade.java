package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.StrategyPort;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

@StrategyPort
@StrategyMaster
public class RootFacade {

    public String get(){
        return "root";
    }

}
