package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import com.charlotte.strategyservice.demo.config.GunStrategyProxy;
import com.charlotte.strategyservice.demo.service.IGunService;

@StrategyMaster(proxy = GunStrategyProxy.class)
public class GunService implements IGunService {

    @Override
    public void shoot() {
        System.out.println("shoot.");
    }
}
