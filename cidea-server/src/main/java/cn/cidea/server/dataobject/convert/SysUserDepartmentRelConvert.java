package cn.cidea.server.dataobject.convert;

import java.util.Date;

import cn.cidea.server.dataobject.dto.SysUserDepartmentRelDTO;
import cn.cidea.server.dataobject.entity.SysUserDepartmentRel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * (SysUserDepartmentRel)Convert
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:00
 */
@Mapper
public interface SysUserDepartmentRelConvert {

    SysUserDepartmentRelConvert INSTANCE = Mappers.getMapper(SysUserDepartmentRelConvert.class);

    SysUserDepartmentRelDTO toDTO(SysUserDepartmentRel entity);

    default List<SysUserDepartmentRelDTO> toDTO(List<SysUserDepartmentRel> entityList) {
        if (entityList == null) {
            return new ArrayList<>(0);
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

