package cn.cidea.module.pay.dataobject.convert;

import cn.cidea.module.pay.dataobject.dto.PayOrderDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderSaveDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUnifiedReqDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUpdateDTO;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import com.github.binarywang.wxpay.bean.request.WxPayReportRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * 支付订单(PayOrder)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-11-30 09:53:12
 */
@Mapper
public interface PayOrderConvert {

    PayOrderConvert INSTANCE = Mappers.getMapper(PayOrderConvert.class);

    PayOrder toSave(PayOrderSaveDTO dto);

    List<PayOrder> toSave(Collection<PayOrderSaveDTO> list);

    PayOrder toUpdate(PayOrderUpdateDTO dto);

    List<PayOrder> toUpdate(Collection<PayOrderUpdateDTO> list);

    PayOrderDTO toDTO(PayOrder entity);

    List<PayOrderDTO> toDTO(Collection<PayOrder> list);

    PayOrderUnifiedReqDTO convert2(PayOrderUpdateDTO dto);
}

