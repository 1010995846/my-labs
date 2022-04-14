package cn.cidea.server.dataobject.covert;

import java.util.Date;

import cn.cidea.server.dataobject.dto.SysResourceDTO;
import cn.cidea.server.dataobject.entity.SysResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysResource)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper
public interface SysResourceCovert {

    SysResourceCovert INSTANCE = Mappers.getMapper(SysResourceCovert.class);

    SysResourceDTO toDTO(SysResource entity);

}

