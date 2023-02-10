package cn.cidea.module.admin.dataobject.convert;


import cn.cidea.module.admin.dataobject.dto.SysTagDTO;
import cn.cidea.module.admin.dataobject.entity.SysTag;
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

