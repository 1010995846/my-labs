package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmBrandDTO;
import cn.cidea.module.pm.dataobject.dto.PmBrandSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmBrandUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmBrand;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * 品牌(PmBrand)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 16:47:59
 */
@Mapper
public interface PmBrandConvert {

    PmBrandConvert INSTANCE = Mappers.getMapper(PmBrandConvert.class);

    PmBrand toSave(PmBrandSaveDTO dto);

    List<PmBrand> toSave(Collection<PmBrandSaveDTO> list);

    PmBrand toUpdate(PmBrandUpdateDTO dto);

    List<PmBrand> toUpdate(Collection<PmBrandUpdateDTO> list);

    PmBrandDTO toDTO(PmBrand entity);

    List<PmBrandDTO> toDTO(Collection<PmBrand> list);

}

