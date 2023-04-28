package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmSkuSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmSku;
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
public interface IPmSkuService extends IService<PmSku> {

    PmSku save(@Valid PmSkuSaveDTO dto);

}

