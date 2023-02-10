package cn.cidea.module.pm.service;


import cn.cidea.module.pm.dataobject.dto.PmMfrSaveDTO;
import cn.cidea.module.pm.dataobject.entity.PmMfr;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 产商(PmMfr)表服务接口
 *
 * @author CIdea
 * @since 2023-01-09 16:48:06
 */
@Validated
public interface IPmMfrService extends IService<PmMfr> {

    PmMfr save(@Valid PmMfrSaveDTO dto);

}

