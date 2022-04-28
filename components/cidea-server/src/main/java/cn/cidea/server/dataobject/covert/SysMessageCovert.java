package cn.cidea.server.dataobject.covert;


import cn.cidea.server.dataobject.dto.SysMessageDTO;
import cn.cidea.server.dataobject.entity.SysMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysMessage)Covert
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Mapper
public interface SysMessageCovert {

    SysMessageCovert INSTANCE = Mappers.getMapper(SysMessageCovert.class);

    SysMessageDTO toDTO(SysMessage entity);

}

