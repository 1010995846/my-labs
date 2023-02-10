package cn.cidea.core.spring.serializer.fastjson;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: CIdea
 */
@Configuration
public class FastjsonSerializerConfiguration {

    @Bean
    @ConditionalOnMissingBean(FastJsonHttpMessageConverter.class)
    public FastJsonHttpMessageConverter FastJsonHttpMessageConverter(){
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = converter.getFastJsonConfig();
        config.setDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        config.setSerializerFeatures(SerializerFeature.DisableCircularReferenceDetect);

        List<MediaType> list = new ArrayList<>();
        list.add(MediaType.APPLICATION_JSON);
        converter.setSupportedMediaTypes(list);
        return converter;
    }

}