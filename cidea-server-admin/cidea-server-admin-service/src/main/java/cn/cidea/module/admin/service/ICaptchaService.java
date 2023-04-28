package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.vo.CaptchaImageVO;

/**
 * 验证码 Service 接口
 */
public interface ICaptchaService {

    /**
     * 获得验证码图片
     *
     * @return 验证码图片
     */
    CaptchaImageVO get();

    /**
     * 获得 uuid 对应的验证码
     *
     * @param uuid 验证码编号
     * @return 验证码
     */
    String getCaptchaCode(String uuid);

    void verify(String uuid, String code);

    /**
     * 删除 uuid 对应的验证码
     *
     * @param uuid 验证码编号
     */
    void deleteCaptchaCode(String uuid);
}
