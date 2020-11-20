package com.charlotte.strategyservice.demo.service.impl;

import com.charlotte.strategyservice.demo.service.IGunService;

public class GunService implements IGunService {

    @Override
    public void shoot() {
        System.out.println("shoot.");
    }
}
