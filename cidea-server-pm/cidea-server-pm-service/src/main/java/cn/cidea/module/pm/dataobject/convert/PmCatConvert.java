package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmCatDTO;
import cn.cidea.module.pm.dataobject.dto.PmCatSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmCatUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmCat;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * 品类(PmCat)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:04
 */
@Mapper
public interface PmCatConvert {

    PmCatConvert INSTANCE = Mappers.getMapper(PmCatConvert.class);

    PmCat toSave(PmCatSaveDTO dto);

    List<PmCat> toSave(Collection<PmCatSaveDTO> list);

    PmCat toUpdate(PmCatUpdateDTO dto);

    List<PmCat> toUpdate(Collection<PmCatUpdateDTO> list);

    PmCatDTO toDTO(PmCat entity);

    List<PmCatDTO> toDTO(Collection<PmCat> list);

}

