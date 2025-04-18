package cn.cidea.framework.common.utils.mq.dataobject.convert;


import cn.cidea.framework.common.utils.mq.dataobject.dto.SysMessageDTO;
import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysMessage)Covert
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Mapper
public interface SysMessageConvert {

    SysMessageConvert INSTANCE = Mappers.getMapper(SysMessageConvert.class);

    SysMessageDTO toDTO(SysMessage entity);

}

