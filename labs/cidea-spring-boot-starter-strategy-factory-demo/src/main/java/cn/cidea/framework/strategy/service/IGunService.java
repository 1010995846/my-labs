package cn.cidea.framework.strategy.service;

import cn.cidea.framework.strategy.config.PollingStrategyRouter;
import cn.cidea.framework.strategy.core.annotation.StrategyAPI;

/**
 * @author Charlotte
 */
@StrategyAPI(router = PollingStrategyRouter.class)
public interface IGunService {

    void shoot();

    default void print(){
        System.out.println("interface");
    }

}
