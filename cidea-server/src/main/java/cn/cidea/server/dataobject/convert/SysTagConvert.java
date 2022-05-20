package cn.cidea.server.dataobject.convert;


import cn.cidea.server.dataobject.dto.SysTagDTO;
import cn.cidea.server.dataobject.entity.SysTag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysTag)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper
public interface SysTagConvert {

    SysTagConvert INSTANCE = Mappers.getMapper(SysTagConvert.class);

    SysTagDTO toDTO(SysTag entity);

}

