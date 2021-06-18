package com.charlotte.lab.mybatis;

import com.charlotte.core.service.CoreServiceApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = CoreServiceApplication.class)
public class LabMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabMybatisApplication.class, args);
    }

}
