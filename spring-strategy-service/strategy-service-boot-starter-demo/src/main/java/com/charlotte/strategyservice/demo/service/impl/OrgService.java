package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.annotation.StrategyMain;
import com.charlotte.strategyservice.demo.config.OrgStrategyProxyConfig;
import com.charlotte.strategyservice.demo.service.IOrgService;

/**
 * @author Charlotte
 */
@StrategyMain
public class OrgService implements IOrgService {
    @Override
    public String getName() {
        return "defaultOrg";
    }
}
