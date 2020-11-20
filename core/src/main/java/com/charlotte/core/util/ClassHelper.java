package com.charlotte.core.util;

import org.springframework.aop.support.AopUtils;

/**
 * @Author: fuchengmu
 * @Date: 2019-07-12 19:16
 */
public class ClassHelper {

    /**
     * 过滤aop代理，获取真实对象类或对象父类的第一个接口
     * @param bean
     * @return
     * @throws Exception
     */
    public static Class getFirstInterface(Object bean) {
        Class realClass = AopUtils.getTargetClass(bean);
        Class<?>[] interfaces = null;
        while (interfaces == null || interfaces.length == 0) {
            // 不断向上回溯，直到找到接口或无父类
            if (realClass == null) {
                // throw new Exception("class " + realClass + " no implementation interface.");
                return null;
            }
            interfaces = realClass.getInterfaces();
            realClass = realClass.getSuperclass();
        }
        return interfaces[0];
    }


}
