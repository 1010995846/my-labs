package com.charlotte.strategyservice.demo;

import com.charlotte.strategyservice.demo.service.IOrgService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Charlotte
 */
@SpringBootApplication
public class StrategyServiceBootStarterDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyServiceBootStarterDemoApplication.class, args);
    }
}
