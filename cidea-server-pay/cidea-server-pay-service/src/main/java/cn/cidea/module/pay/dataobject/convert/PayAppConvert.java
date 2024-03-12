package cn.cidea.module.pay.dataobject.convert;

import cn.cidea.module.pay.dataobject.dto.PayAppDTO;
import cn.cidea.module.pay.dataobject.dto.PayAppSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayApp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PayAppConvert {

    PayAppConvert INSTANCE = Mappers.getMapper(PayAppConvert.class);

    PayApp toEntity(PayAppSaveDTO dto);

    PayAppDTO toDTO(PayApp entity);
    
    List<PayAppDTO> toDTO(Collection<PayApp> entity);

}

