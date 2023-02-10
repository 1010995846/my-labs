package cn.cidea.module.admin.dataobject.convert;

import cn.cidea.module.admin.dataobject.dto.SysUserDepartmentRelDTO;
import cn.cidea.module.admin.dataobject.entity.SysUserDeptRel;
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

    SysUserDepartmentRelDTO toDTO(SysUserDeptRel entity);

    default List<SysUserDepartmentRelDTO> toDTO(List<SysUserDeptRel> entityList) {
        if (entityList == null) {
            return new ArrayList<>(0);
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

