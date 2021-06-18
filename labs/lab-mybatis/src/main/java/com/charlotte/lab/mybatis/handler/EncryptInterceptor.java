package com.charlotte.lab.mybatis.handler;

import com.charlotte.lab.mybatis.annotation.SensitiveData;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Charlotte
 */
@Slf4j
@Component
@Intercepts({
    @Signature(type = ParameterHandler.class, method = "setParameters", args = PreparedStatement.class),})
public class EncryptInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // @Signature 指定了 type= parameterHandler 后，这里的 invocation.getTarget() 便是parameterHandler
        // 若指定ResultSetHandler ，这里则能强转为ResultSetHandler
        ParameterHandler parameterHandler = (ParameterHandler) invocation.getTarget();
        // 获取参数对像，即 mapper 中 paramsType 的实例
        Field parameterField = parameterHandler.getClass().getDeclaredField("parameterObject");
        parameterField.setAccessible(true);
        // 取出实例
        Object parameterObject = parameterField.get(parameterHandler);
        if (parameterObject != null) {
            Class<?> parameterObjectClass = parameterObject.getClass();
            // 校验该实例的类是否被@SensitiveData所注解
            SensitiveData sensitiveData = AnnotationUtils.findAnnotation(parameterObjectClass, SensitiveData.class);
            if (Objects.nonNull(sensitiveData)) {
                // 取出当前当前类所有字段，传入加密方法
                Field[] declaredFields = parameterObjectClass.getDeclaredFields();
//                encryptUtil.encrypt(declaredFields, parameterObject);
                // TODO Charlotte: 2021/6/16 加密
            }
        }
        return invocation.proceed();
    }

    /**
     * 切记配置，否则当前拦截器不会加入拦截器链
     */
    @Override
    public Object plugin(Object o) {
        return Plugin.wrap(o, this);
    }

    //自定义配置写入，没有自定义配置的可以直接置空此方法
    @Override
    public void setProperties(Properties properties) {
    }
}
