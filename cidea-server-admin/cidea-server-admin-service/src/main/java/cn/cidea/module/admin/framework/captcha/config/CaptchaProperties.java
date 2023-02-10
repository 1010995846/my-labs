package cn.cidea.module.admin.framework.captcha.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Data
@Validated
@ConfigurationProperties(prefix = "captcha")
public class CaptchaProperties {

    private static final Boolean ENABLE_DEFAULT = true;

    /**
     * 是否开启
     */
    private Boolean enable = ENABLE_DEFAULT;
    /**
     * 验证码的过期时间
     */
    @NotNull(message = "验证码的过期时间不为空")
    private Duration timeout = Duration.ofMinutes(1);
    /**
     * 验证码的高度
     */
    @NotNull(message = "验证码的高度不能为空")
    private Integer height = 30;
    /**
     * 验证码的宽度
     */
    @NotNull(message = "验证码的宽度不能为空")
    private Integer width = 100;

}
