package cn.cidea.module.admin.dataobject.convert;

import cn.cidea.module.admin.dataobject.dto.SysRoleDTO;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SysRoleConvert {

    SysRoleConvert INSTANCE = Mappers.getMapper(SysRoleConvert.class);

    SysRole convert(SysRoleDTO saveDTO);

    SysRoleDTO toDTO(SysRole role);

    List<SysRoleDTO> toDTO(Collection<SysRole> collection);

}
