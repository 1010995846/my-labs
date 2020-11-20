package com.charlotte.lab.proxy;

import org.springframework.stereotype.Component;

@Component
public class AbstractAni implements Ani{
    @Override
    public void run() {
        System.out.println("ani is running.");
    }
}
