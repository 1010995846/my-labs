package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmPropSaveDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.module.pm.dataobject.entity.PmProp;
import org.springframework.validation.annotation.Validated;

/**
 * (PmProp)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 13:58:26
 */
@Validated
public interface IPmPropService extends IService<PmProp> {

    PmProp save(PmPropSaveDTO data);
}

