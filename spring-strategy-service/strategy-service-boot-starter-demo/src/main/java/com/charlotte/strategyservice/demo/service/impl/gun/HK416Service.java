package com.charlotte.strategyservice.demo.service.impl.gun;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.service.impl.GunService;

/**
 * @author Charlotte
 */
@StrategyBranch("HK416")
public class HK416Service extends GunService {

    @Override
    public void shoot() {
        System.out.println("HK416");
    }
}
