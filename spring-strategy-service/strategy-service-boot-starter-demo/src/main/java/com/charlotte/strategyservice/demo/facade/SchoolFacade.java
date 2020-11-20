package com.charlotte.strategyservice.demo.facade;

import com.charlotte.strategyservice.annotation.StrategyBranch;

@StrategyBranch("school")
public class SchoolFacade extends OrgFacade {
    @Override
    public void print() {
        System.out.println("school print.");
    }
}
