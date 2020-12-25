package com.charlotte.strategyservice.demo.facade;

import com.charlotte.strategyservice.annotation.StrategyMain;
import org.springframework.context.annotation.Primary;

@Primary
@StrategyMain
public class OrgFacade {

    public void print(){
        System.out.println("org print.");
    }

}
