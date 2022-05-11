package cn.cidea.server.dataobject.covert;

import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Collection;

@Mapper
public interface RoleConvert {

    RoleConvert INSTANCE = Mappers.getMapper(RoleConvert.class);

    SysRole convert(SysRoleDTO saveDTO);

    SysRoleDTO toDTO(SysRole role);

    Collection<SysRoleDTO> toDTO(Collection<SysRole> collection);

}
