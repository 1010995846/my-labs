package com.charlotte.strategyservice.demo.facade.stand;

import org.springframework.stereotype.Service;

//@Service
public class RightFacade extends RootFacade {

    @Override
    public String get() {
        return "right";
    }
}
