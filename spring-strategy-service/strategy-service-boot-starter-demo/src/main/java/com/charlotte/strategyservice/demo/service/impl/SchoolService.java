package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyBranch;

/**
 * @author Charlotte
 */
@StrategyBranch("school")
public class SchoolService extends OrgService {

    @Override
    public String getName() {
        return "school";
    }
}
