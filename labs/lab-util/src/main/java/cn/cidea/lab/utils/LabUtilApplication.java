package cn.cidea.lab.utils;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LabUtilApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LabUtilApplication.class, args);
        return;
    }

}
