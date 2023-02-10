package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmProductSkuSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmProductSku;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * (PmProductSku)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 16:48:18
 */
@Validated
public interface IPmProductSkuService extends IService<PmProductSku> {

    PmProductSku save(@Valid PmProductSkuSaveDTO dto);

}

