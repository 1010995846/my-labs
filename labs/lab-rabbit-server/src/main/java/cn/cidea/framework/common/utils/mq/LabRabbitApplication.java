package cn.cidea.framework.common.utils.mq;

import cn.cidea.module.admin.AdminApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {AdminApplication.class, LabRabbitApplication.class})
public class LabRabbitApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabRabbitApplication.class, args);
    }

}
