package cn.cidea.module.admin.dataobject.convert;

import cn.cidea.module.admin.dataobject.vo.CaptchaImageVO;
import cn.hutool.captcha.AbstractCaptcha;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CaptchaConvert {

    CaptchaConvert INSTANCE = Mappers.getMapper(CaptchaConvert.class);

    default CaptchaImageVO convert(String uuid, AbstractCaptcha captcha) {
        return CaptchaImageVO.builder().uuid(uuid).img(captcha.getImageBase64()).build();
    }

}
