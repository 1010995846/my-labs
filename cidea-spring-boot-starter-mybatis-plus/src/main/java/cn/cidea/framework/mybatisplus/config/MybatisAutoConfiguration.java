package cn.cidea.framework.mybatisplus.config;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author Charlotte
 */
@Configuration
@MapperScan(basePackages = {"cn.cidea.**.dal.mysql"})
public class MybatisAutoConfiguration {

    /**
     * 插件
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(MybatisPlusInterceptor.class)
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        return new MybatisPlusInterceptor();
    }

    /**
     * 分页插件
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(PaginationInnerInterceptor.class)
    public PaginationInnerInterceptor paginationInnerInterceptor(@Autowired MybatisPlusInterceptor interceptor) {
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return paginationInnerInterceptor;
    }

    /**
     * ID生成器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(IdentifierGenerator.class)
    public DefaultIdentifierGenerator getIdentifierGenerator() {
        DefaultIdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
        return identifierGenerator;
    }

}
