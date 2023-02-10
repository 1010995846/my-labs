package cn.cidea.module.pm.dataobject.convert;

import cn.cidea.module.pm.dataobject.dto.PmProductSaveDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductUpdateDTO;
import cn.cidea.module.pm.dataobject.dto.PmProductValDTO;
import cn.cidea.module.pm.dataobject.entity.PmProduct;
import cn.cidea.module.pm.dataobject.dto.PmProductDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品(PmProduct)表服务对象转化类
 *
 * @author CIdea
 * @since 2023-01-09 16:48:16
 */
@Mapper
public interface PmProductConvert {

    PmProductConvert INSTANCE = Mappers.getMapper(PmProductConvert.class);

    @Mapping(target = "propVal", expression = "java(propVal(dto))")
    PmProduct toSave(PmProductSaveDTO dto);

    List<PmProduct> toSave(Collection<PmProductSaveDTO> list);

    @Mapping(target = "propVal", expression = "java(propVal(dto))")
    PmProduct toUpdate(PmProductUpdateDTO dto);

    List<PmProduct> toUpdate(Collection<PmProductUpdateDTO> list);

    PmProductDTO toDTO(PmProduct entity);

    List<PmProductDTO> toDTO(Collection<PmProduct> list);

    default Map<String, String> propVal(PmProductUpdateDTO dto){
        if(dto.getProperties() == null){
            return Collections.emptyMap();
        }
        return dto.getProperties().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> Optional.ofNullable(entry.getValue()).map(PmProductValDTO::getVal).orElse(null)));
    }

}

