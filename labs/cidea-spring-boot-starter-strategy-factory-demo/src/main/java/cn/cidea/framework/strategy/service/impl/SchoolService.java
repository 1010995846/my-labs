package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.core.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("school")
public class SchoolService extends OrgMasterService {

    @Override
    public String getName() {
        return "school";
    }
}
