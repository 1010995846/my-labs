package cn.cidea.core.spring.serializer.masking.jackson;

import cn.cidea.core.spring.serializer.masking.Masked;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
public class JacksonMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        Masked annotation = am.getAnnotation(Masked.class);
        if (annotation == null) {
            return null;
        }
        return new JacksonMaskingSerializer(annotation.maskFunc());
    }

}
