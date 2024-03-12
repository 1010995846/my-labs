package cn.cidea.module.order.dataobject.convert;

import cn.cidea.module.order.dataobject.dto.OrderDTO;
import cn.cidea.module.order.dataobject.dto.OrderSaveDTO;
import cn.cidea.module.order.dataobject.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

/**
 * @author: CIdea
 */
@Mapper
public interface OrderConvert {

    OrderConvert INSTANCE = Mappers.getMapper(OrderConvert.class);

    Order toEntity(OrderSaveDTO dto);

    OrderDTO toDTO(Order dto);

    Collection<OrderDTO> toDTO(Collection<Order> dto);

}
