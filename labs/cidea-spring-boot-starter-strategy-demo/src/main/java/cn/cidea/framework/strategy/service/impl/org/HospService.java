package cn.cidea.framework.strategy.service.impl.org;

import cn.cidea.framework.strategy.annotation.HospBranch;
import cn.cidea.framework.strategy.service.impl.OrgService;

/**
 * @author Charlotte
 */
@HospBranch
public class HospService extends OrgService {
    @Override
    public String getName() {
        return "hosp";
    }
}
