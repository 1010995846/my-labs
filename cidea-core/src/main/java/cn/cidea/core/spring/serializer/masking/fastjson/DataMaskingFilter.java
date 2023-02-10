package cn.cidea.core.spring.serializer.masking.fastjson;

import cn.cidea.core.spring.serializer.masking.DataMasking;
import cn.cidea.core.spring.serializer.masking.DataMaskingFunc;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author: CIdea
 */
@Slf4j
public class DataMaskingFilter implements ValueFilter  {

    @Override
    public Object process(Object object, String name, Object value) {
        if (null == value || !(value instanceof String) || ((String) value).length() == 0) {
            return value;
        }
        try {
            Field field = object.getClass().getDeclaredField(name);
            DataMasking masking;
            if (String.class != field.getType() || (masking = field.getAnnotation(DataMasking.class)) == null) {
                return value;
            }
            DataMaskingFunc func = masking.maskFunc();
            return func.getOperation().mask((String) value);
        } catch (NoSuchFieldException e) {
            log.warn("ValueDesensitizeFilter the class {} has no field {}", object.getClass(), name);
        }
        return value;
    }

}
