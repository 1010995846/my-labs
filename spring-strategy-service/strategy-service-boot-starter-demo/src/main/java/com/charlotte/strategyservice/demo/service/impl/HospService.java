package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.service.IOrgService;

/**
 * @author Charlotte
 */
@StrategyBranch("hosp")
public class HospService extends OrgService {
    @Override
    public String getName() {
        return "hosp";
    }
}
