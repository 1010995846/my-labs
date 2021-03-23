package com.charlotte.strategyservice.demo.service.impl.org;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.annotation.HospBranch;
import com.charlotte.strategyservice.demo.service.IOrgService;
import com.charlotte.strategyservice.demo.service.impl.OrgService;

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
