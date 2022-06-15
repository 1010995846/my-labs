package cn.cidea.framework.strategy.service.impl;

import cn.cidea.framework.strategy.annotation.HospBranch;

/**
 * @author Charlotte
 */
@HospBranch
public class HospService extends OrgMasterService {
    @Override
    public String getName() {
        return "hosp";
    }
}
