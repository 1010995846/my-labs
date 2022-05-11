package cn.cidea.framework.strategy.service.impl.gun;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.impl.GunService;

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
