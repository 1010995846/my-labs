package cn.cidea.server.dal.redis;

import cn.cidea.server.dataobject.dto.LoginUserDTO;
import cn.cidea.server.security.config.SecurityProperties;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * {@link LoginUserDTO} 的 RedisDAO
 *
 * @author 芋道源码
 */
@Repository
public class LoginUserRedisDAO {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SecurityProperties securityProperties;

    public LoginUserDTO get(String sessionId) {
        String redisKey = formatKey(sessionId);
        return JSONObject.parseObject(stringRedisTemplate.opsForValue().get(redisKey), LoginUserDTO.class);
    }

    public void set(String sessionId, LoginUserDTO loginUser) {
        String redisKey = formatKey(sessionId);
        stringRedisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(loginUser),
                securityProperties.getSessionTimeout());
    }

    public void delete(String sessionId) {
        String redisKey = formatKey(sessionId);
        stringRedisTemplate.delete(redisKey);
    }

    private static String formatKey(String sessionId) {
        return String.format(RedisKeyConstants.LOGIN_USER.getKeyTemplate(), sessionId);
    }

}