package com.charlotte.strategyservice.demo.service.impl.org;

import com.charlotte.strategyservice.demo.annotation.HospBranch;
import com.charlotte.strategyservice.demo.service.impl.OrgService;

/**
 * @author Charlotte
 */
@HospBranch
public class HospService extends OrgService {
    @Override
    public String getName(String id) {
        return "hosp";
    }
}
