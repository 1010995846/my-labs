package cn.cidea.server.service.system;

import cn.cidea.server.dal.mysql.ISysUserRoleMapper;
import cn.cidea.server.dataobject.entity.SysUserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysUserRole)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:30
 */
@Slf4j
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<ISysUserRoleMapper, SysUserRole> implements ISysUserRoleService {

}
