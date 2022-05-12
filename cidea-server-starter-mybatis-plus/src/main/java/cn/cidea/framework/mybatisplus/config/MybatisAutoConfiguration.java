package cn.cidea.framework.mybatisplus.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
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
public class MybatisAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public DefaultIdentifierGenerator getIdentifierGenerator(){
        DefaultIdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
        return identifierGenerator;
    }

    @Configuration
    public static class IdWorkerConfiguration {

        public IdWorkerConfiguration(IdentifierGenerator identifierGenerator) {
            IdWorker.setIdentifierGenerator(identifierGenerator);
        }
    }

}
