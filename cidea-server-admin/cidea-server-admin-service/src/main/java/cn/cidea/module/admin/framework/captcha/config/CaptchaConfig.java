package cn.cidea.module.admin.framework.captcha.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CaptchaProperties.class)
public class CaptchaConfig {
}
