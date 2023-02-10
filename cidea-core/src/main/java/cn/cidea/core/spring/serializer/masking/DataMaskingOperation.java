package cn.cidea.core.spring.serializer.masking;

/**
 * 脱敏接口方法定义
 * @author: CIdea
 */
public interface DataMaskingOperation {

    String MASK_CHAR = "*";

    String mask(String content, String maskChar);

    default String mask(String content){
        return mask(content, MASK_CHAR);
    }

}
