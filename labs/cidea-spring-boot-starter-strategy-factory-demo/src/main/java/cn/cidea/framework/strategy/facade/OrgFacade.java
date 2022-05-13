package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.Strategy;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

/**
 * @author Charlotte
 */
@Strategy
@StrategyMaster
public class OrgFacade {

    public void print(){
        System.out.println("master print.");
    }

}
