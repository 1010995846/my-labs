package cn.cidea.module.order.service;

import cn.cidea.module.order.dataobject.dto.OrderSaveDTO;
import cn.cidea.module.order.dataobject.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author: CIdea
 */
public interface IOrderService extends IService<Order> {

    Order save(OrderSaveDTO saveDTO);

    void payed(String id);
}
