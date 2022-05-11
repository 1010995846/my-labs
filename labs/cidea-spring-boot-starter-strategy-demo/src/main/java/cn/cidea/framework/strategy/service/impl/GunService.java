package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.config.GunStrategyProxy;
import cn.cidea.framework.strategy.service.IGunService;
import cn.cidea.framework.strategy.annotation.StrategyMaster;

@StrategyMaster(proxy = GunStrategyProxy.class)
public class GunService implements IGunService {

    @Override
    public void shoot() {
        System.out.println("shoot.");
    }
}
