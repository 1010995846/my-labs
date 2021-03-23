package com.charlotte.strategyservice.demo.service.impl.gun;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.service.impl.GunService;

/**
 * @author Charlotte
 */
@StrategyBranch("AR15")
public class AR15Service extends GunService {

    @Override
    public void shoot() {
        System.out.println("AR15");
    }
}
