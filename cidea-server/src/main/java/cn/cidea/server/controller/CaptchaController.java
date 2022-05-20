package cn.cidea.server.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.server.controller.vo.CaptchaImageRespVO;
import cn.cidea.server.service.common.ICaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/system/captcha")
public class CaptchaController {

    @Resource
    private ICaptchaService captchaService;

    @GetMapping("/get-image")
    public Response<CaptchaImageRespVO> getCaptchaImage() {
        return Response.success(captchaService.getCaptchaImage());
    }

}
