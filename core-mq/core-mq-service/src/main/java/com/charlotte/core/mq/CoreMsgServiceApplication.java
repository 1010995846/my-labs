package com.charlotte.core.mq;

import com.charlotte.core.service.CoreServiceApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {CoreServiceApplication.class, CoreMsgServiceApplication.class})
@MapperScan("com.charlotte.core.mq.mapper")
@EnableDiscoveryClient
@EnableFeignClients
public class CoreMsgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreMsgServiceApplication.class, args);
    }

}
