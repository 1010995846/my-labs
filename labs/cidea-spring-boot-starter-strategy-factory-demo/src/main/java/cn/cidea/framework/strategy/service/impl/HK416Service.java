package cn.cidea.framework.strategy.service.impl;


import cn.cidea.framework.strategy.core.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("HK416")
public class HK416Service extends GunMasterService {

    @Override
    public void shoot() {
        System.out.println("HK416");
    }
}
