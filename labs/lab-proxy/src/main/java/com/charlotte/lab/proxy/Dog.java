package com.charlotte.lab.proxy;

import lombok.Data;

@Data
//@Component
public class Dog extends AbstractAni {

    private String name = "二哈";

    @Override
    public void run(){
        System.out.println("Dog is running.");
    }


}
