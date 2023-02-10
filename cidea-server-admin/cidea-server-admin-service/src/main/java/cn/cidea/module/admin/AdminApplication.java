package cn.cidea.module.admin;

import cn.cidea.core.serializer.FastjsonSerializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Charlotte
 */
@SpringBootApplication
@EnableScheduling
@FastjsonSerializer
public class AdminApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AdminApplication.class, args);
    }

}
