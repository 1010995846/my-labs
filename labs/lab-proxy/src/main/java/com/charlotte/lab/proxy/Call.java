package com.charlotte.lab.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Call {

    @Autowired
    private Ani ani;

    public void call(){
        System.out.println("call");
        ani.run();
//        od(null, null);
    }

    public void od(Dog dog1, Dog dog2){

    }
    public void od(Dog dog1, Cat cat1){

    }
}
