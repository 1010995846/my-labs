package cn.cidea.server.dataobject.convert;

import java.util.Date;

import cn.cidea.server.dataobject.dto.SysDepartmentDTO;
import cn.cidea.server.dataobject.entity.SysDepartment;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * (SysDepartment)Convert
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:26
 */
@Mapper
public interface SysDepartmentConvert {

    SysDepartmentConvert INSTANCE = Mappers.getMapper(SysDepartmentConvert.class);

    SysDepartmentDTO toDTO(SysDepartment entity);

    default List<SysDepartmentDTO> toDTO(List<SysDepartment> entityList) {
        if (entityList == null) {
            return new ArrayList<>(0);
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

