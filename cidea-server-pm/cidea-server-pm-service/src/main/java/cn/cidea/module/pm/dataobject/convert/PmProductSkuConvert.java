package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmProductSkuDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductSkuSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductSkuUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmProductSku;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * (PmProductSku)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:21
 */
@Mapper
public interface PmProductSkuConvert {

    PmProductSkuConvert INSTANCE = Mappers.getMapper(PmProductSkuConvert.class);

    PmProductSku toSave(PmProductSkuSaveDTO dto);

    List<PmProductSku> toSave(Collection<PmProductSkuSaveDTO> list);

    PmProductSku toUpdate(PmProductSkuUpdateDTO dto);

    List<PmProductSku> toUpdate(Collection<PmProductSkuUpdateDTO> list);

    PmProductSkuDTO toDTO(PmProductSku entity);

    List<PmProductSkuDTO> toDTO(Collection<PmProductSku> list);

}

