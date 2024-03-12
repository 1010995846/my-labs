package cn.cidea.core.spring.serializer.masking;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.Map;

/**
 * 脱敏接口方法定义
 * @author: CIdea
 */
public abstract class MaskMethod {

    private static final Map<Class, MaskMethod> functions = new HashMap<>();

    public abstract CharSequence mask(CharSequence content);

    public Object mask(Object content){
        if(content == null){
            return null;
        }
        if(content instanceof CharSequence){
            return mask((CharSequence)content);
        }
        return content;
    }

    @SneakyThrows
    public static MaskMethod get(Class<? extends MaskMethod> clazz){
        MaskMethod func = functions.get(clazz);
        if(func == null){
            func = clazz.newInstance();
            functions.put(clazz, func);
        }
        return func;
    }

}
