package cn.cidea.module.pay.service;


import cn.cidea.module.pay.dataobject.dto.PayOrderDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderSaveDTO;
import cn.cidea.module.pay.dataobject.dto.PayOrderUpdateDTO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.module.pay.dataobject.entity.PayOrder;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 支付订单(PayOrder)表服务接口
 *
 * @author CIdea
 * @since 2023-11-30 09:53:09
 */
@Validated
public interface IPayOrderService extends IService<PayOrder> {

    /**
     * 创建
     * @param dto
     * @return
     */
    PayOrder create(PayOrderSaveDTO dto);

    /**
     * 提交支付
     * @param dto
     * @return
     */
    PayOrder submit(@Valid PayOrderUpdateDTO dto);

    /**
     * 取消订单
     * @param id
     * @return
     */
    void cancel(Long id);

    IPage<PayOrder> listByAdmin(PayOrderDTO dto, IPage<PayOrder> page);
}

