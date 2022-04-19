package cn.cidea.server.service.common;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.server.controller.vo.CaptchaImageRespVO;
import cn.cidea.server.dataobject.covert.CaptchaConvert;
import cn.cidea.server.framework.captcha.config.CaptchaProperties;
import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.cidea.server.dal.redis.CaptchaRedisDAO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 验证码 Service 实现类
 */
@Service
public class CaptchaServiceImpl implements ICaptchaService {

    @Resource
    private CaptchaProperties captchaProperties;

    @Resource
    private CaptchaRedisDAO captchaRedisDAO;

    @Override
    public CaptchaImageRespVO getCaptchaImage() {
        Boolean enable = captchaProperties.getEnable();
        // if (!Boolean.TRUE.equals(enable)) {
        //     return CaptchaImageRespVO.builder().enable(enable).build();
        // }
        // 生成验证码
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(captchaProperties.getWidth(), captchaProperties.getHeight());
        // 缓存到 Redis 中
        String uuid = IdUtil.fastSimpleUUID();
        captchaRedisDAO.set(uuid, captcha.getCode(), captchaProperties.getTimeout());
        // 返回
        return CaptchaConvert.INSTANCE.convert(uuid, captcha).setEnable(enable);
    }

    @Override
    public String getCaptchaCode(String uuid) {
        return captchaRedisDAO.get(uuid);
    }

    @Override
    public void verify(String uuid, String code) {
        // 如果验证码关闭，则不进行校验
        if (!captchaProperties.getEnable()) {
            return;
        }
        String captchaCode = getCaptchaCode(uuid);
        Assert.VALID.isNotBlank(captchaCode, "验证码不存在");
        Assert.VALID.isTrue(captchaCode.equals(code), "验证码不正确");
    }

    @Override
    public void deleteCaptchaCode(String uuid) {
        captchaRedisDAO.delete(uuid);
    }

}
