package cn.cidea.module.admin.controller;

import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.dataobject.vo.CaptchaImageVO;
import cn.cidea.module.admin.service.ICaptchaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/sys/captcha")
public class CaptchaController {

    @Resource
    private ICaptchaService captchaService;

    @GetMapping("/get")
    public Response<CaptchaImageVO> get() {
        return Response.success(captchaService.get());
    }

}
