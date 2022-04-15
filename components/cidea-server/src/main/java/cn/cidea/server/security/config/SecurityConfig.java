package cn.cidea.server.security.config;

import cn.cidea.server.security.filter.AuthenticationTokenFilter;
import cn.cidea.server.service.auth.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Charlotte
 */
@Configuration
// 开启对 Spring Security 注解的方法，进行权限验证
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private ILoginService loginService;
    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;
    /**
     * Token 认证过滤器 Bean
     */
    @Autowired
    private AuthenticationTokenFilter authenticationTokenFilter;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService)
            // 不指定时默认使用动态DelegatingPasswordEncoder
                // .passwordEncoder(bCryptPasswordEncoder())
        ;
    }

    /**
     * 强散列哈希加密实现
     */
    // @Bean
    // public PasswordEncoder bCryptPasswordEncoder() {
    //     // PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    //     PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();
    //     return passwordEncoder;
    // }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // 开启跨域
                .cors().and()
                // CRSF禁用，因为不使用session
                .csrf().disable()
                // 基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        httpSecurity
                // 认证失败处理类
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
        // <Z>
        // httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
        // <P> 添加 JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity
                // 过滤请求
                .authorizeRequests()
                // <Y> 对于登录login 验证码captchaImage 允许匿名访问
                .antMatchers("/sys/user/login", "/captchaImage").anonymous()
                // 无需认证
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()
                // 跳过认证
                .antMatchers("/profile/**").anonymous()
                .antMatchers("/common/download**").anonymous()
                .antMatchers("/swagger-ui.html").anonymous()
                .antMatchers("/swagger-resources/**").anonymous()
                .antMatchers("/webjars/**").anonymous()
                .antMatchers("/*/api-docs").anonymous()
                .antMatchers("/druid/**").anonymous()
                // 除上面外的所有请求全部需要鉴权认证
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();
    }

    /**
     * 加入IOC
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
