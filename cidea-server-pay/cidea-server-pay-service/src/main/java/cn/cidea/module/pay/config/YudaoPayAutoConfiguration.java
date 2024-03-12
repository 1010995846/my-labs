package cn.cidea.module.pay.config;

import cn.cidea.module.pay.client.PayClientFactory;
import cn.cidea.module.pay.client.impl.PayClientFactoryImpl;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 支付配置类
 */
@AutoConfiguration
@EnableConfigurationProperties(PayProperties.class)
public class YudaoPayAutoConfiguration {

    @Bean
    public PayClientFactory payClientFactory() {
        return new PayClientFactoryImpl();
    }

}
