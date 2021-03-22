package com.charlotte.lab.proxy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
public class CallService {

    @Autowired
    private IAniService ani;

    public void call() {
        System.out.println("call");
        ani.run();
//        od(null, null);
    }

    public void od(DogService dog1, DogService dog2) {

    }

    public void od(DogService dog1, CatService catService1) {

    }
}
