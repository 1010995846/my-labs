package com.charlotte.strategyservice.demo.facade;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import org.springframework.context.annotation.Primary;

@Primary
@StrategyMaster
public class OrgFacade {

    public void print(){
        System.out.println("org print.");
    }

}
