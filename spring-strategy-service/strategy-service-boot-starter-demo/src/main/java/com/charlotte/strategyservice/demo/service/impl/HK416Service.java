package com.charlotte.strategyservice.demo.service.impl;

public class HK416Service extends GunService {

    @Override
    public void shoot() {
        System.out.println("HK416");
    }
}
