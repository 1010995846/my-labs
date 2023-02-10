package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmPropDTO;
import cn.cidea.module.pm.dataobject.dto.PmPropSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmPropUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmProp;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * (PmProp)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 14:52:54
 */
@Mapper
public interface PmPropConvert {

    PmPropConvert INSTANCE = Mappers.getMapper(PmPropConvert.class);

    PmProp toSave(PmPropSaveDTO dto);

    List<PmProp> toSave(Collection<PmPropSaveDTO> list);

    PmProp toUpdate(PmPropUpdateDTO dto);

    List<PmProp> toUpdate(Collection<PmPropUpdateDTO> list);

    PmPropDTO toDTO(PmProp entity);

    List<PmPropDTO> toDTO(Collection<PmProp> list);

}

