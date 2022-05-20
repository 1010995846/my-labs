package cn.cidea.server.dataobject.convert;

import cn.cidea.server.dataobject.dto.PersonIdentificationDTO;
import cn.cidea.server.dataobject.entity.PersonIdentification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 个人证件(PersonIdentification)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper
public interface PersonIdentificationConvert {

    PersonIdentificationConvert INSTANCE = Mappers.getMapper(PersonIdentificationConvert.class);

    PersonIdentificationDTO toDTO(PersonIdentification entity);

}

