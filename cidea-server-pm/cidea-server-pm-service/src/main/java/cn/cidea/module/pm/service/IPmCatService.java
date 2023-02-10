package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmCatSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmCat;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 品类(PmCat)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 16:48:01
 */
@Validated
public interface IPmCatService extends IService<PmCat> {

    PmCat save(@Valid PmCatSaveDTO dto);

}

