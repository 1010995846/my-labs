package cn.cidea.core.spring.serializer.fastjson;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author: CIdea
 */
@Configuration
@EnableWebMvc
public class FastjsonSerializerMvcConfigurer implements WebMvcConfigurer {

    @Autowired
    private FastJsonHttpMessageConverter fastJsonHttpMessageConverter;

    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(0, fastJsonHttpMessageConverter);
    }

}