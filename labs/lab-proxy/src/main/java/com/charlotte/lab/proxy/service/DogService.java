package com.charlotte.lab.proxy.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class DogService extends AbstractAniService {

    private String name = "二哈";

    @Override
    public void run(){
        System.out.println("Dog is running.");
    }

}
