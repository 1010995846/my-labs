package cn.cidea.module.admin.dataobject.convert;

import cn.cidea.module.admin.dataobject.dto.PersonIdentificationDTO;
import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

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

    PersonIdentification toEntity(PersonIdentificationDTO entity);

    List<PersonIdentification> toEntity(List<PersonIdentificationDTO> list);

}

