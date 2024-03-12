package cn.cidea.module.pay.dataobject.convert;

import cn.cidea.module.pay.dataobject.dto.PayChannelDTO;
import cn.cidea.module.pay.dataobject.dto.PayChannelSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayChannel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PayChannelConvert {

    PayChannelConvert INSTANCE = Mappers.getMapper(PayChannelConvert.class);

    PayChannel toEntity(PayChannelSaveDTO dto);

    PayChannelDTO toDTO(PayChannel entity);
    
    List<PayChannelDTO> toDTO(Collection<PayChannel> entity);

}

