package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmProductSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmProduct;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 产品(PmProduct)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 16:48:11
 */
@Validated
public interface IPmProductService extends IService<PmProduct> {

    PmProduct save(@Valid PmProductSaveDTO dto);

}

