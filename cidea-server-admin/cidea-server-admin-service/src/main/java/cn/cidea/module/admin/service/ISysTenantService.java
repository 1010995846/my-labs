package cn.cidea.module.admin.service;


import cn.cidea.framework.security.core.service.TenantFrameworkService;
import cn.cidea.module.admin.dataobject.dto.SysTenantSaveDTO;
import cn.cidea.module.admin.dataobject.entity.SysTenant;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表服务接口
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:24
 */
@Validated
@Transactional(readOnly = true)
public interface ISysTenantService extends IService<SysTenant>, TenantFrameworkService {

    SysTenant save(SysTenantSaveDTO saveDTO);
}
