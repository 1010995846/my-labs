package cn.cidea.server.dal.redis;


import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.framework.redis.core.RedisKeyDefine;

import java.time.Duration;

import static cn.cidea.framework.redis.core.RedisKeyDefine.KeyTypeEnum.STRING;

/**
 * System Redis Key 枚举类
 *
 * @author 芋道源码
 */
public interface RedisKeyConstants {

    RedisKeyDefine CAPTCHA_CODE = new RedisKeyDefine("验证码的缓存",
            // 参数为 uuid
            "captcha_code:%s",
            STRING, String.class, RedisKeyDefine.TimeoutTypeEnum.DYNAMIC);

    RedisKeyDefine LOGIN_USER = new RedisKeyDefine("登录用户的缓存",
            // 参数为 sessionId
            "login_user:%s",
            STRING, SysUser.class, RedisKeyDefine.TimeoutTypeEnum.DYNAMIC);

    // 注意，它是被 JustAuth 的 justauth.type.prefix 使用到
    RedisKeyDefine SOCIAL_AUTH_STATE = new RedisKeyDefine("社交登陆的 state",
            // 参数为 state
            "social_auth_state:%s",
            STRING, String.class, Duration.ofHours(24));

}
