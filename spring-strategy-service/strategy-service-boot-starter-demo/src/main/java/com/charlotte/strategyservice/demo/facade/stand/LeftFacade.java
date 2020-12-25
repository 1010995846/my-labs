package com.charlotte.strategyservice.demo.facade.stand;

import org.springframework.stereotype.Service;

//@Service
public class LeftFacade extends RootFacade {

    @Override
    public String get() {
        return "left";
    }
}
