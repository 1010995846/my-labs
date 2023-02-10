package cn.cidea.module.admin.dataobject.convert;

import java.util.Date;

import cn.cidea.module.admin.dataobject.dto.SysTenantDTO;
import cn.cidea.module.admin.dataobject.dto.SysTenantSaveDTO;
import cn.cidea.module.admin.dataobject.entity.SysTenant;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)Convert
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:23
 */
@Mapper
public interface SysTenantConvert {

    SysTenantConvert INSTANCE = Mappers.getMapper(SysTenantConvert.class);

    SysTenantDTO toDTO(SysTenant entity);

    default List<SysTenantDTO> toDTO(List<SysTenant> entityList) {
        if (entityList == null) {
            return new ArrayList<>(0);
        }
        return entityList.stream().map(this::toDTO).collect(Collectors.toList());
    }

    SysTenant toDO(SysTenantSaveDTO saveDTO);
}

