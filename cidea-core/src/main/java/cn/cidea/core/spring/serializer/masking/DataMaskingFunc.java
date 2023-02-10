package cn.cidea.core.spring.serializer.masking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏类型及其实现
 * @author: CIdea
 */
@Getter
@AllArgsConstructor
public enum DataMaskingFunc {

    /**
     *  脱敏转换器
     */
    NO_MASK((str, maskChar) -> str),
    ALL_MASK((str, maskChar) -> {
        if (StringUtils.isNotBlank(str)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                sb.append(StringUtils.isNotBlank(maskChar) ? maskChar : DataMaskingOperation.MASK_CHAR);
            }
            return sb.toString();
        } else {
            return str;
        }
    });

    private final DataMaskingOperation operation;

}
