package cn.cidea.lab.proxy.service;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class AbstractAniService implements IAniService {
    @Override
    public void run() {
        System.out.println("ani is running.");
    }
}
