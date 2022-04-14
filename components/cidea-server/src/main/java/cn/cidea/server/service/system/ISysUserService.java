package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysUser;
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
