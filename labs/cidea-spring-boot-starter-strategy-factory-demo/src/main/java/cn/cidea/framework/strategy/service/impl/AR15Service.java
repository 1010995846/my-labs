package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.IGunService;

/**
 * @author Charlotte
 */
@StrategyBranch("AR15")
public class AR15Service implements IGunService {

    @Override
    public void shoot() {
        System.out.println("AR15");
    }
}
