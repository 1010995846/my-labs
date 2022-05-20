package cn.cidea.server.dataobject.convert;

import cn.cidea.framework.common.utils.CollectionSteamUtils;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.server.dataobject.dto.SysUserDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import cn.cidea.server.dataobject.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


/**
 * (SysUser)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper(imports = {CollectionSteamUtils.class, SysRole.class})
public interface SysUserConvert {

    SysUserConvert INSTANCE = Mappers.getMapper(SysUserConvert.class);

    SysUserDTO toDTO(SysUser entity);

    @Mapping(target = "roleIds", expression = "java(CollectionSteamUtils.mapSet(user.getRoles(), SysRole::getId))")
    LoginUserDTO toLoginDTO(SysUser user);

}

