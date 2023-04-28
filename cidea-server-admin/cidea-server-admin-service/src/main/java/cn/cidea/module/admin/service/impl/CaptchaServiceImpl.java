package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dataobject.vo.CaptchaImageVO;
import cn.cidea.module.admin.dal.redis.CaptchaRedisDAO;
import cn.cidea.module.admin.dataobject.convert.CaptchaConvert;
import cn.cidea.module.admin.framework.captcha.config.CaptchaProperties;
import cn.cidea.module.admin.service.ICaptchaService;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 验证码 Service 实现类
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService {

    @Resource
    private CaptchaProperties properties;

    @Resource
    private CaptchaRedisDAO redisDAO;

    @Override
    public CaptchaImageVO get() {
        Boolean enable = properties.getEnable();
        // if (!Boolean.TRUE.equals(enable)) {
        //     return CaptchaImageRespVO.builder().enable(enable).build();
        // }
        // 生成验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(properties.getWidth(), properties.getHeight());
        // 缓存到 Redis 中
        String uuid = IdUtil.fastSimpleUUID();
        redisDAO.set(uuid, captcha.getCode(), properties.getTimeout());
        // 返回
        return CaptchaConvert.INSTANCE.convert(uuid, captcha).setEnable(enable).setTimeout(properties.getTimeout());
    }

    @Override
    public String getCaptchaCode(String uuid) {
        return redisDAO.get(uuid);
    }

    @Override
    public void verify(String uuid, String code) {
        // 如果验证码关闭，则不进行校验
        if (!properties.getEnable()) {
            return;
        }
        String captchaCode = getCaptchaCode(uuid);
        Assert.VALID.isNotBlank(captchaCode, "验证码不存在");
        Assert.VALID.isTrue(captchaCode.equals(code), "验证码不正确");
    }

    @Override
    public void deleteCaptchaCode(String uuid) {
        redisDAO.delete(uuid);
    }

}
