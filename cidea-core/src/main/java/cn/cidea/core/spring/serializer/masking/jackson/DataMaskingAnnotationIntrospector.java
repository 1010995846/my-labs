package cn.cidea.core.spring.serializer.masking.jackson;

import cn.cidea.core.spring.serializer.masking.DataMasking;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: CIdea
 */
@Slf4j
public class DataMaskingAnnotationIntrospector extends NopAnnotationIntrospector {

    @Override
    public Object findSerializer(Annotated am) {
        DataMasking annotation = am.getAnnotation(DataMasking.class);
        if (annotation == null) {
            return null;
        }
        return new DataMaskingSerializer(annotation.maskFunc().getOperation());
    }

}
