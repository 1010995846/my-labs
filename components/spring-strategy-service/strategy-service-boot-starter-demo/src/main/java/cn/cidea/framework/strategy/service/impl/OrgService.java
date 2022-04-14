package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.annotation.StrategyMaster;
import cn.cidea.framework.strategy.service.IOrgService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Charlotte
 */
@StrategyMaster
@Transactional(readOnly = false)
public class OrgService implements IOrgService {

    @Override
    public String getName(String id) {
        return "defaultOrg";
    }
}
