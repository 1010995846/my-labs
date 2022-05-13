package cn.cidea.framework.strategy.service;

import cn.cidea.framework.strategy.config.PollingStrategyRoute;
import cn.cidea.framework.strategy.core.annotation.Strategy;

/**
 * @author Charlotte
 */
@Strategy(route = PollingStrategyRoute.class)
public interface IGunService {

    void shoot();

}
