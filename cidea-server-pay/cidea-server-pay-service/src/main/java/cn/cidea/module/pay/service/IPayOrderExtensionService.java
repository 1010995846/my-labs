package cn.cidea.module.pay.service;


import cn.cidea.module.pay.dataobject.dto.PayOrderExtensionSaveDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表服务接口
 *
 * @author CIdea
 * @since 2023-11-30 09:53:13
 */
@Validated
public interface IPayOrderExtensionService extends IService<PayOrderExtension> {

    PayOrderExtension save(@Valid PayOrderExtensionSaveDTO dto);

}

