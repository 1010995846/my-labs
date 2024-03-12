package cn.cidea.core.spring.serializer.masking;

import org.apache.commons.lang3.StringUtils;

/**
 * @author: CIdea
 */
public class MaskAll extends MaskMethod {

    private String MASK_CHAR = "*";
    
    @Override
    public CharSequence mask(CharSequence content) {
        if (StringUtils.isBlank(content)) {
            return content;
        }
        // 脱敏执行
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            sb.append(c == ' ' ? c : MASK_CHAR);
        }
        return sb.toString();
    }
    
}
