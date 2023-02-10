package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmMfrDTO;
import cn.cidea.module.pm.dataobject.dto.PmMfrSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmMfrUpdateDTO;
import cn.cidea.module.pm.dataobject.entity.PmMfr;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

/**
 * 产商(PmMfr)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:08
 */
@Mapper
public interface PmMfrConvert {

    PmMfrConvert INSTANCE = Mappers.getMapper(PmMfrConvert.class);

    PmMfr toSave(PmMfrSaveDTO dto);

    List<PmMfr> toSave(Collection<PmMfrSaveDTO> list);

    PmMfr toUpdate(PmMfrUpdateDTO dto);

    List<PmMfr> toUpdate(Collection<PmMfrUpdateDTO> list);

    PmMfrDTO toDTO(PmMfr entity);

    List<PmMfrDTO> toDTO(Collection<PmMfr> list);

}

