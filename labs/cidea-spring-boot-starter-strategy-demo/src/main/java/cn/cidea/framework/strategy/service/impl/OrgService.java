package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.annotation.StrategyMaster;
import cn.cidea.framework.strategy.service.IOrgService;

/**
 * @author Charlotte
 */
@StrategyMaster
public class OrgService implements IOrgService {

    @Override
    public String getName() {
        return "defaultOrg";
    }
}
