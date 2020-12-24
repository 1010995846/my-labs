package com.charlotte.lab.proxy.service;

import org.springframework.stereotype.Component;

@Component
public class AbstractAniService implements IAniService {
    @Override
    public void run() {
        System.out.println("ani is running.");
    }
}
