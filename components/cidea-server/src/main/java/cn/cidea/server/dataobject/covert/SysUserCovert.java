package cn.cidea.server.dataobject.covert;

import cn.cidea.server.dataobject.dto.LoginUserDTO;
import cn.cidea.server.dataobject.dto.SysUserDTO;
import cn.cidea.server.dataobject.entity.SysUser;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysUser)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper
public interface SysUserCovert {

    SysUserCovert INSTANCE = Mappers.getMapper(SysUserCovert.class);

    SysUserDTO toDTO(SysUser entity);

    LoginUserDTO toLoginDTO(SysUser user);
}

