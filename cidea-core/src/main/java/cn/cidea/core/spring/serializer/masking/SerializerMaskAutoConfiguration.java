package cn.cidea.core.spring.serializer.masking;

import cn.cidea.core.spring.serializer.masking.fastjson.FastjsonMaskingFilter;
import cn.cidea.core.spring.serializer.masking.jackson.JacksonMaskingAnnotationIntrospector;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Spring序列化加入脱敏拦截
 * @author: CIdea
 */
@Configuration(proxyBeanMethods = false)
public class SerializerMaskAutoConfiguration implements Serializable {

    /**
     * Jackson
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass({Jackson2ObjectMapperBuilder.class})
    static class JacksonObjectMapperConfiguration {

        JacksonObjectMapperConfiguration(ObjectMapper objectMapper) {
            AnnotationIntrospector introspector = objectMapper.getSerializationConfig().getAnnotationIntrospector();
            introspector = AnnotationIntrospectorPair.pair(introspector, new JacksonMaskingAnnotationIntrospector());
            objectMapper.setAnnotationIntrospector(introspector);
        }

    }

    /**
     * FastJson
     */
    @Configuration(proxyBeanMethods = false)
    @ConditionalOnBean({FastJsonHttpMessageConverter.class})
    static class FastJsonConfiguration {

        public FastJsonConfiguration(FastJsonHttpMessageConverter converter) {
            FastJsonConfig config = converter.getFastJsonConfig();
            SerializeFilter[] filters = config.getSerializeFilters();
            if(filters == null){
                filters = new SerializeFilter[1];
            } else {
                filters = Arrays.copyOf(filters, filters.length + 1);
            }
            filters[filters.length - 1] = new FastjsonMaskingFilter();
            config.setSerializeFilters(filters);
        }

    }
}
