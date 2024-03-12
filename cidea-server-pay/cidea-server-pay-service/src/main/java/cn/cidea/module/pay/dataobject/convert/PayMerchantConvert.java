package cn.cidea.module.pay.dataobject.convert;

import cn.cidea.module.pay.dataobject.dto.PayMerchantDTO;
import cn.cidea.module.pay.dataobject.dto.PayMerchantSaveDTO;
import cn.cidea.module.pay.dataobject.entity.PayMerchant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PayMerchantConvert {

    PayMerchantConvert INSTANCE = Mappers.getMapper(PayMerchantConvert.class);

    PayMerchant toEntity(PayMerchantSaveDTO dto);

    PayMerchantDTO toDTO(PayMerchant entity);
    
    List<PayMerchantDTO> toDTO(Collection<PayMerchant> entity);

}

