package com.charlotte.strategyservice.demo.service.impl.org;


import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.annotation.P1Branch;
import com.charlotte.strategyservice.demo.annotation.P2Branch;
import com.charlotte.strategyservice.demo.service.IOrgService;

/**
 * @author Charlotte
 */
@P1Branch
@P2Branch
//@StrategyBranch("p1")
//@StrategyBranch("p2")
public class PluralService implements IOrgService {
    @Override
    public String getName(String id) {
        return "plural";
    }
}
