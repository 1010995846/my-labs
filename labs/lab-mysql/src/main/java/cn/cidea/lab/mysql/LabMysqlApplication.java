package cn.cidea.lab.mysql;

import cn.cidea.server.ServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = ServerApplication.class)
public class LabMysqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(LabMysqlApplication.class, args);
    }

}
