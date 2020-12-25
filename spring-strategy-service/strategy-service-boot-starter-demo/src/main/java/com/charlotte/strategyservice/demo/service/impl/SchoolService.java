package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
@StrategyBranch("school")
public class SchoolService extends OrgService {

    @Override
    public String getName() {
        return "school";
    }
}
