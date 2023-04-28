package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.StrategyAPI;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

/**
 * @author Charlotte
 */
@StrategyAPI
@StrategyMaster
public class OrgFacade {

    public void print(){
        System.out.println("master print.");
    }

}
