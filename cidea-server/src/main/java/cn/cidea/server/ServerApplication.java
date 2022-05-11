package cn.cidea.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Charlotte
 */
@SpringBootApplication
@EnableScheduling
public class ServerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ServerApplication.class, args);
    }

}
