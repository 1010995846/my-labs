package cn.cidea.module.admin.dataobject.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录结果的枚举类
 */
@Getter
@AllArgsConstructor
public enum LoginResult {

    SUCCESS(0), // 成功
    BAD_CREDENTIALS(10), // 账号或密码不正确
    USER_DISABLED(20), // 用户被禁用
    CAPTCHA_NOT_FOUND(30), // 图片验证码不存在
    CAPTCHA_CODE_ERROR(31), // 图片验证码不正确

    UNKNOWN_ERROR(100), // 未知异常
    ;

    /**
     * 结果
     */
    private final Integer result;

}
