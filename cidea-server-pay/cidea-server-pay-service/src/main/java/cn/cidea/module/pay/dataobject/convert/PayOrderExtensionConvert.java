package cn.cidea.module.pay.dataobject.convert;

import cn.cidea.module.pay.dataobject.dto.PayOrderExtensionDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderExtensionSaveDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderExtensionUpdateDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUpdateDTO;
import cn.cidea.module.pay.dataobject.entity.PayOrderExtension;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * order每次发起支付的记录和凭证(PayOrderExtension)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:14
 */
@Mapper
public interface PayOrderExtensionConvert {

    PayOrderExtensionConvert INSTANCE = Mappers.getMapper(PayOrderExtensionConvert.class);

    PayOrderExtension toEntity(PayOrderUpdateDTO dto);

    PayOrderExtensionDTO toDTO(PayOrderExtension entity);

    List<PayOrderExtensionDTO> toDTO(Collection<PayOrderExtension> list);

}

