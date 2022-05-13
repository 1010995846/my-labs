package cn.cidea.framework.strategy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author Charlotte
 */
@SpringBootApplication
public class StrategyDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyDemoApplication.class, args);
    }
}
