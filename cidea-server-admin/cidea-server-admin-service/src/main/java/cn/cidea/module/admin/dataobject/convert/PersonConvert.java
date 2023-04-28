package cn.cidea.module.admin.dataobject.convert;


import cn.cidea.module.admin.dataobject.dto.PersonDTO;
import cn.cidea.module.admin.dataobject.dto.PersonSaveDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 个人信息(Person)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 13:39:20
 */
@Mapper
public interface PersonConvert {

    PersonConvert INSTANCE = Mappers.getMapper(PersonConvert.class);

    PersonDTO toDTO(Person entity);

    Person toEntity(PersonSaveDTO dto);
}

