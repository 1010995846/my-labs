package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.entity.PmBrand;
import cn.cidea.module.pm.dataobject.dto.PmBrandSaveDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 品牌(PmBrand)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 16:47:56
 */
@Validated
public interface IPmBrandService extends IService<PmBrand> {

    PmBrand save(@Valid PmBrandSaveDTO dto);

}

