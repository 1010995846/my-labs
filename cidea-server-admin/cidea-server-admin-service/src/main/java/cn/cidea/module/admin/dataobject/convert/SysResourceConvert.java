package cn.cidea.module.admin.dataobject.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import cn.cidea.module.admin.dataobject.dto.SysResourceDTO;
import cn.cidea.module.admin.dataobject.entity.SysResource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * (SysResource)Covert
 *
 * @author yechangfei
 * @since 2022-04-14 11:29:16
 */
@Mapper
public interface SysResourceConvert {

    SysResourceConvert INSTANCE = Mappers.getMapper(SysResourceConvert.class);

    SysResourceDTO toDTO(SysResource entity);

    default List<SysResourceDTO> toDTO(Collection<SysResource> list){
        if(list == null){
            return new ArrayList<>(0);
        }
        return list.stream().map(this::toDTO).collect(Collectors.toList());
    }

}

