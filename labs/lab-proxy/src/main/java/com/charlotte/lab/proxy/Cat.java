package com.charlotte.lab.proxy;

import lombok.Data;

@Data
//@Component
public class Cat extends AbstractAni{

    private String name = "黑";

    @Override
    public void run() {
        System.out.println("cat is running.");
    }
}
