package cn.cidea.server.dataobject.covert;

import java.util.Date;

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
public interface PersonIdentificationCovert {

    PersonIdentificationCovert INSTANCE = Mappers.getMapper(PersonIdentificationCovert.class);

    PersonIdentificationDTO toDTO(PersonIdentification entity);

}

