package cn.cidea.core.spring.serializer.fastjson;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * web启用fastjson序列化
 * @author: CIdea
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FastjsonSerializerConfiguration.class, FastjsonSerializerMvcConfigurer.class})
public @interface EnableFastjsonSerializer {
}
