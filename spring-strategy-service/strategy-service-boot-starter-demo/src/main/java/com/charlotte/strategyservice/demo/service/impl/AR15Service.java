package com.charlotte.strategyservice.demo.service.impl;

public class AR15Service extends GunService{

    @Override
    public void shoot() {
        System.out.println("AR15");
    }
}
