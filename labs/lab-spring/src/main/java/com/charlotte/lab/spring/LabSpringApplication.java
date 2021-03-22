package com.charlotte.lab.spring;

import com.charlotte.core.service.CoreServiceApplication;
import com.charlotte.core.service.service.IUserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScan(basePackageClasses = {CoreServiceApplication.class})
public class LabSpringApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(LabSpringApplication.class, args);
        return;
    }

}
