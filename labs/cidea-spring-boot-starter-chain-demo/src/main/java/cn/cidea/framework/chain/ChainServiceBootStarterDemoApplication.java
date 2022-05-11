package cn.cidea.framework.chain;

import cn.cidea.framework.chain.service.IMsgChainService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Map;

/**
 * @author Charlotte
 */
// 方式一
@SpringBootApplication
public class ChainServiceBootStarterDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ChainServiceBootStarterDemoApplication.class, args);
        Map<String, IMsgChainService> beansOfType = context.getBeansOfType(IMsgChainService.class);
        IMsgChainService service = context.getBean(IMsgChainService.class);
        service.alter();
        return;
    }

}
