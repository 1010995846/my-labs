package cn.cidea.core.spring.serializer.masking.fastjson;

import cn.cidea.core.spring.serializer.masking.Masked;
import cn.cidea.core.spring.serializer.masking.MaskMethod;
import com.alibaba.fastjson.serializer.ValueFilter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: CIdea
 */
@Slf4j
public class FastjsonMaskingFilter implements ValueFilter  {

    private static final Map<Class, MaskMethod> functions = new HashMap<>();

    @SneakyThrows
    @Override
    public Object process(Object object, String name, Object value) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            Masked masking;
            if ((masking = field.getAnnotation(Masked.class)) == null) {
                return value;
            }
            return MaskMethod.get(masking.maskFunc()).mask(value);
        } catch (NoSuchFieldException e) {
            log.warn("ValueDesensitizeFilter the class {} has no field {}", object.getClass(), name);
        }
        return value;
    }

}
