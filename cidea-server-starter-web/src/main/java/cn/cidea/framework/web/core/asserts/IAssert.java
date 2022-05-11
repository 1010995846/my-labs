package cn.cidea.framework.web.core.asserts;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;

/**
 * 断言工具
 * 由异常枚举类实现{@link #build}接口创建异常，断言不通过则创建异常并抛出
 * 支持链式表达
 * @author Charlotte
 */
public interface IAssert {

    /**
     * 创建异常
     * @param args
     * @return
     */
    RuntimeException build(String... args);


    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default IAssert nonNull(Object obj, String... args) {
        return isTrue(Objects.nonNull(obj), args);
    }

    /**
     * 字符串非空
     * @param cs
     * @param args
     * @return
     */
    default IAssert isNotBlank(CharSequence cs, String... args) {
        return isTrue(StringUtils.isNotBlank(cs), args);
    }

    /**
     * 集合非空
     * @param coll
     * @param args
     * @return
     */
    default IAssert isNotEmpty(Collection coll, String... args) {
        return isTrue(CollectionUtils.isNotEmpty(coll), args);
    }

    /**
     * 为真
     * @param b
     * @param args
     * @return
     */
    default IAssert isTrue(boolean b, String... args){
        if(!b){
            throw build(args);
        }
        return this;
    }

    default IAssert isFalse(Boolean b, String... args){
        if(!Boolean.FALSE.equals(b)){
            throw build(args);
        }
        return this;
    }

}
