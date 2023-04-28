package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * (SysUserRole)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:30
 */
@Transactional(readOnly = true)
public interface ISysUserRoleService extends IService<SysUserRole> {

}
