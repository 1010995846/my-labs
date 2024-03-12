package cn.cidea.lab.spring;

import cn.cidea.lab.spring.spi.TestApplicationContextInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class LabSpringApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(LabSpringApplication.class);
        // application.addInitializers(new TestApplicationContextInitializer());
        ConfigurableApplicationContext context = application.run(args);
        return;
    }

}
