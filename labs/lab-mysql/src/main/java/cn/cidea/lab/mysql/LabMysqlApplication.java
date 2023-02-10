package cn.cidea.lab.mysql;

import cn.cidea.module.admin.AdminApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = AdminApplication.class)
public class LabMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabMysqlApplication.class, args);
    }

}
