package cn.cidea.lab.utils.modules.idempotent.token;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author: CIdea
 */
public class IdempotentAspect {

    @Resource
    private RedisIdempotentStorage redisIdempotentStorage;

    @Pointcut("@annotation(cn.cidea.lab.utils.modules.idempotent.token.Idempotent)")
    public void idempotent() {
    }

    @Around("idempotent()")
    public Object methodAround(ProceedingJoinPoint point, Idempotent idempotent) throws Throwable {
        Map<String, Object> paramValue = getParamValue(point);
        Object token = paramValue.get("token");
        if(token != null && !redisIdempotentStorage.delete(token.toString())) {
            // 传入的token已使用
            throw new RuntimeException("请勿重复点击！");
        }
        return point.proceed();
    }

    public static Map<String, Object> getParamValue(ProceedingJoinPoint joinPoint) {
        Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        Map<String, Object> param = new HashMap<>(paramNames.length);

        for (int i = 0; i < paramNames.length; i++) {
            param.put(paramNames[i], paramValues[i]);
        }
        return param;
    }


    public static Object getFieldValue(Object obj, String name) throws Exception {
        Field[] fields = obj.getClass().getDeclaredFields();
        Object object = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().toUpperCase().equals(name.toUpperCase())) {
                object = field.get(obj);
                break;
            }
        }
        return object;
    }
}
