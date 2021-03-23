package com.charlotte.strategyservice.demo.facade.org;

import com.charlotte.strategyservice.annotation.StrategyBranch;
import com.charlotte.strategyservice.demo.facade.OrgFacade;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@StrategyBranch("hosp")
public class HospFacade extends OrgFacade {

    @Override
    public void print() {
        System.out.println("hosp print.");
    }
}
