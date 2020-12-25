package com.charlotte.strategyservice.demo.facade.stand;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class RootFacade {

    public String get(){
        return "root";
    }

}
