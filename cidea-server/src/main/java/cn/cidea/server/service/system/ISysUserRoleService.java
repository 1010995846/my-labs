package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysUserRole;
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
