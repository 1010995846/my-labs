package com.charlotte.strategyservice.demo.facade;

import com.charlotte.strategyservice.annotation.StrategyMaster;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@StrategyMaster
public class RootFacade {

    public String get(){
        return "root";
    }

}
