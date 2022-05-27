package cn.cidea.server.service;


import cn.cidea.framework.security.core.service.TenantFrameworkService;
import cn.cidea.server.dataobject.dto.SysTenantSaveDTO;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import cn.cidea.server.dataobject.entity.SysTenant;
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
