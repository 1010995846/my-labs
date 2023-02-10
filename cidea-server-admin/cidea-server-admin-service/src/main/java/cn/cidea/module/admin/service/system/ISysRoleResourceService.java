package cn.cidea.module.admin.service.system;


import cn.cidea.module.admin.dataobject.entity.SysRoleResource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * (SysRoleResource)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Transactional(readOnly = true)
public interface ISysRoleResourceService extends IService<SysRoleResource> {

}
