package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import cn.cidea.framework.strategy.service.IOrgService;

/**
 * @author Charlotte
 */
@StrategyMaster
public class OrgMasterService implements IOrgService {

    @Override
    public String getName(String id) {
        return "defaultOrg";
    }
}
