package com.charlotte.core.service.enums;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 断言工具
 * 由异常枚举类实现{@link #newException}接口创建异常，断言不通过则创建异常并抛出
 * 支持链式表达
 * @author Charlotte
 */
public interface Assert {

    /**
     * 创建异常
     * @param args
     * @return
     */
    RuntimeException newException(String... args);


    /**
     * <p>断言对象<code>obj</code>非空。如果对象<code>obj</code>为空，则抛出异常
     * <p>异常信息<code>message</code>支持传递参数方式，避免在判断之前进行字符串拼接操作
     *
     * @param obj 待判断对象
     * @param args message占位符对应的参数列表
     */
    default Assert nonNull(Object obj, String... args) {
        return isTrue(Objects.nonNull(obj), args);
    }

    /**
     * 字符串非空
     * @param cs
     * @param args
     * @return
     */
    default Assert isNotBlank(CharSequence cs, String... args) {
        return isTrue(StringUtils.isNotBlank(cs), args);
    }

    /**
     * 集合非空
     * @param coll
     * @param args
     * @return
     */
    default Assert isNotEmpty(Collection coll, String... args) {
        return isTrue(CollectionUtils.isNotEmpty(coll), args);
    }

    /**
     * 为真
     * @param b
     * @param args
     * @return
     */
    default Assert isTrue(boolean b, String... args){
        if(!b){
            throw newException(args);
        }
        return this;
    }

}
