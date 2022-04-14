package cn.cidea.lab.proxy.service;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class CatService extends AbstractAniService {

    private String name = "黑";

    @Override
    public void run() {
        System.out.println("cat is running.");
    }
}
