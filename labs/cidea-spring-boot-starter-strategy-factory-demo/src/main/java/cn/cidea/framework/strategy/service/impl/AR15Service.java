package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("AR15")
public class AR15Service extends GunMasterService {

    @Override
    public void shoot() {
        System.out.println("AR15");
    }
}
