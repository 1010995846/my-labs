package cn.cidea.framework.common.utils.mq;

import cn.cidea.server.ServerApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Charlotte
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {ServerApplication.class, CoreMsgServiceApplication.class})
public class CoreMsgServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreMsgServiceApplication.class, args);
    }

}
