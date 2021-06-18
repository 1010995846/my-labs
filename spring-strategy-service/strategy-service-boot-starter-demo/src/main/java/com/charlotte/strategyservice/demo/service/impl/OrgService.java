package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import com.charlotte.strategyservice.demo.service.IOrgService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Charlotte
 */
@StrategyMaster
@Transactional(readOnly = false)
public class OrgService implements IOrgService {
    @Override
    public String getName() {
        return "defaultOrg";
    }
}
