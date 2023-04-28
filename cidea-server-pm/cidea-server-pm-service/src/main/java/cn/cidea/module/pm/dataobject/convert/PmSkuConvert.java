package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmSkuDTO;
import cn.cidea.module.pm.dataobject.dto.PmSkuSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmSkuUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmSku;
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
public interface PmSkuConvert {

    PmSkuConvert INSTANCE = Mappers.getMapper(PmSkuConvert.class);

    PmSku toSave(PmSkuSaveDTO dto);

    List<PmSku> toSave(Collection<PmSkuSaveDTO> list);

    PmSku toUpdate(PmSkuUpdateDTO dto);

    List<PmSku> toUpdate(Collection<PmSkuUpdateDTO> list);

    PmSkuDTO toDTO(PmSku entity);

    List<PmSkuDTO> toDTO(Collection<PmSku> list);

}

