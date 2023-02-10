package cn.cidea.core.utils;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Charlotte
 */
public class StringFormatUtils {

    // private static final char CHAR_DECIMAL_POINT = '.';
    // private static final char CHAR_ZERO = '0';
    //
    // public static int getInt(String str) {
    //     String sb = getNumberStr(str, false);
    //     return NumberUtils.createInteger(sb);
    // }
    //
    // /**
    //  * eg: 1.00 -> 1; 1.DS -> 1; 1.DS8 -> 1.8; 1.DS8.6 -> 1.8
    //  *
    //  * @param str
    //  * @param decimal   是否保留小数
    //  * @return
    //  */
    // public static String getNumberStr(String str, boolean decimal) {
    //     String regEx = "[^(0-9|.)]";
    //     Pattern p = Pattern.compile(regEx);
    //     // 移除所有非数字
    //     str = p.matcher(str).replaceAll("").trim();
    //     StringBuilder sb = new StringBuilder();
    //     // 小数标志位
    //     char[] chars = str.toCharArray();
    //     boolean hasDecimal = false;
    //     for (int i = 0; i < chars.length; i++) {
    //         char c = chars[i];
    //         boolean hasNext = (i < chars.length - 1);
    //         if (c == CHAR_ZERO && sb.length() == 0 && hasNext && chars[i + 1] != CHAR_DECIMAL_POINT) {
    //             // 首位不可为0，跳过，除非后面没有或是小数点
    //             continue;
    //         }
    //         if (c == CHAR_DECIMAL_POINT) {
    //             if (!decimal || hasDecimal) {
    //                 // 不保留或已有，跳过第一/二个小数点之后的所有数字
    //                 break;
    //             }
    //             hasDecimal = true;
    //         }
    //         sb.append(c);
    //     }
    //     if (hasDecimal) {
    //         // 移除小数后缀0
    //         for (int length = sb.length(); length > 0; length--) {
    //             if (sb.charAt(length - 1) != CHAR_ZERO) {
    //                 break;
    //             }
    //             sb.deleteCharAt(length - 1);
    //         }
    //         if (sb.charAt(sb.length() - 1) == CHAR_DECIMAL_POINT) {
    //             sb.deleteCharAt(sb.length() - 1);
    //         }
    //     }
    //     return sb.toString();
    // }

    /**
     * 驼峰转下划线（guava）
     * @param str
     * @return
     */
    public static String humpToUnderline(String str) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, str);
    }

    /**
     * 下划线转驼峰（guava）
     * @param str
     * @return
     */
    public static String underlineToHump(String str) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, str);
    }

    public static String encodeChinese(String str) throws UnsupportedEncodingException {
        Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(str);
        while (matcher.find()) {
            String tmp = matcher.group();
            str = str.replaceAll(tmp, URLEncoder.encode(tmp, "UTF-8"));
        }
        return str.replace(" ", "%20");
    }
}
