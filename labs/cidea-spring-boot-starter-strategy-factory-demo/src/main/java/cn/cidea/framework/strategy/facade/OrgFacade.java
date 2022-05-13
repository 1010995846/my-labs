package cn.cidea.framework.strategy.facade;


import cn.cidea.framework.strategy.core.annotation.StrategyPort;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;

/**
 * @author Charlotte
 */
@StrategyPort
@StrategyMaster
public class OrgFacade {

    public void print(){
        System.out.println("master print.");
    }

}
