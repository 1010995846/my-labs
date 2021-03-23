package com.charlotte.strategyservice.demo.facade.org;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.facade.OrgFacade;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@StrategyBranch("school")
public class SchoolFacade extends OrgFacade {
    @Override
    public void print() {
        System.out.println("school print.");
    }
}
