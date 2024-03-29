package cn.cidea.framework.security.core.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.Duration;

/**
 * @author Charlotte
 */
@Data
@Validated
@Configuration
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    /**
     * HTTP 请求时，访问令牌的请求 Header
     */
    @NotEmpty(message = "Session Header 不能为空")
    private String sessionHeader = "Authorization";

    /**
     * Session 过期时间
     *
     * 当 User 用户超过当前时间未操作，则 Session 会过期
     */
    @NotNull(message = "Session 过期时间不能为空")
    private Duration sessionTimeout;

    /**
     * mock 模式的开关
     */
    @NotNull(message = "mock 模式的开关不能为空")
    private Boolean mockEnable;
    /**
     * mock 模式的秘钥
     * 一定要配置秘钥，保证安全性
     */
    @NotEmpty(message = "mock 模式的秘钥不能为空") // 这里设置了一个默认值，因为实际上只有 mockEnable 为 true 时才需要配置。
    private String mockSecret = "yudaoyuanma";

}
