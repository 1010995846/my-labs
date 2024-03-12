package cn.cidea.framework.strategy;

import cn.cidea.framework.strategy.core.annotation.StrategyScan;
import cn.cidea.framework.strategy2.IOutService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@StrategyScan(basePackageClasses = IOutService.class)
@ComponentScan(basePackageClasses = {StrategyDemoApplication.class, IOutService.class})
public class StrategyDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(StrategyDemoApplication.class, args);
    }
}
