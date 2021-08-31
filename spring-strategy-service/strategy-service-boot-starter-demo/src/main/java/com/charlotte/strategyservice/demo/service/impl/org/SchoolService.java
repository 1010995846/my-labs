package com.charlotte.strategyservice.demo.service.impl.org;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.service.impl.OrgService;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
@StrategyBranch("school")
public class SchoolService extends OrgService {

    @Override
    public String getName(String id) {
        return "school";
    }
}
