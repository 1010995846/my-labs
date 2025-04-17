package cn.cidea.framework.strategy.service.impl;


import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.IGunService;

/**
 * @author Charlotte
 */
@StrategyBranch("HK416")
public class HK416Service implements IGunService {

    @Override
    public void shoot() {
        System.out.println("HK416");
    }
}
