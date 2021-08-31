package com.charlotte.strategyservice.demo.service.impl.org;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.service.IOrgService;

@StrategyBranch("ext")
public class ExtService implements IOrgService {
    @Override
    public String getName(String id) {
        return "ext";
    }
}
