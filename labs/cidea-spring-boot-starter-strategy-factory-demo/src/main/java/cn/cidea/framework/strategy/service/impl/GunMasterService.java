package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.strategy.service.IGunService;

@StrategyMaster
public class GunMasterService implements IGunService {

    @Override
    public void shoot() {
        System.out.println("shoot.");
    }

    @Override
    public void print() {
        System.out.println("master");
    }
}
