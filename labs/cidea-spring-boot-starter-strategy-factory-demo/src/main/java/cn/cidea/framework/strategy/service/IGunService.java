package cn.cidea.framework.strategy.service;

import cn.cidea.framework.strategy.config.PollingStrategyRoute;
import cn.cidea.framework.strategy.core.annotation.StrategyPort;

/**
 * @author Charlotte
 */
@StrategyPort(route = PollingStrategyRoute.class)
public interface IGunService {

    void shoot();

}
