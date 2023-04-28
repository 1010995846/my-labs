package cn.cidea.framework.security.config;

import cn.cidea.framework.security.core.filter.AuthenticationTokenFilter;
import cn.cidea.framework.security.core.handler.AuthenticationEntryPointImpl;
import cn.cidea.framework.security.core.properties.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author Charlotte
 */
@Configuration
public class SecurityAutoConfiguration {

    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPointImpl();
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter(){
        return new AuthenticationTokenFilter();
    }

}
