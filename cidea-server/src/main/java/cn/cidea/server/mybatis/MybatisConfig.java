package cn.cidea.server.mybatis;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author Charlotte
 */
@Configuration
@MapperScan(basePackages = {"cn.cidea.*.dal.mysql"})
public class MybatisConfig {

    @Bean
    @ConditionalOnMissingBean(DefaultIdentifierGenerator.class)
    public DefaultIdentifierGenerator getIdentifierGenerator(){
        return new DefaultIdentifierGenerator();
    }

}
