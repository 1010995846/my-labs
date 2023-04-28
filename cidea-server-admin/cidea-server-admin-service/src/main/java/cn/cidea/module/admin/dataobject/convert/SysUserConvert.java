package cn.cidea.module.admin.dataobject.convert;

import cn.cidea.core.utils.CollSteamUtils;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.module.admin.dataobject.dto.SysUserDTO;
import cn.cidea.module.admin.dataobject.dto.SysUserRegisterDTO;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * (SysUser)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper(imports = {CollSteamUtils.class, SysRole.class})
public interface SysUserConvert {

    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    SysUserDTO toDTO(SysUser entity);

    @Mapping(target = "roleIds", expression = "java(cn.cidea.core.utils.CollSteamUtils.toSet(user.getRoles(), SysRole::getId))")
    LoginUserDTO toLoginDTO(SysUser user);

    SysUser toEntity(SysUserRegisterDTO dto);
}

