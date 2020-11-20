package com.charlotte.strategyservice.demo.facade;

import com.charlotte.strategyservice.annotation.StrategyBranch;

@StrategyBranch("hosp")
public class HospFacade extends OrgFacade{

    @Override
    public void print() {
        System.out.println("hosp print.");
    }
}
