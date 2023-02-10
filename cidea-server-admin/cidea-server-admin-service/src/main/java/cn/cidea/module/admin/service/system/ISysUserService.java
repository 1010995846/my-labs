package cn.cidea.module.admin.service.system;


import cn.cidea.module.admin.dataobject.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * (SysUser)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Validated
@Transactional(readOnly = true)
public interface ISysUserService extends IService<SysUser> {

    SysUser getUserByUsername(String username);

}
