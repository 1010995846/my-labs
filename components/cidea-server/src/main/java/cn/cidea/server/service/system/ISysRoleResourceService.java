package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysRoleResource;
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
