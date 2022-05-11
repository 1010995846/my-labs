package cn.cidea.framework.strategy.service.impl.gun;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.impl.GunService;

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
