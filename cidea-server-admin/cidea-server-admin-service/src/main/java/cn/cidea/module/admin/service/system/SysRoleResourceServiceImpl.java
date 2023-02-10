package cn.cidea.module.admin.service.system;

import cn.cidea.module.admin.dal.mysql.ISysRoleResourceMapper;
import cn.cidea.module.admin.dataobject.entity.SysRoleResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysRoleResource)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Slf4j
@Service
public class SysRoleResourceServiceImpl extends ServiceImpl<ISysRoleResourceMapper, SysRoleResource> implements ISysRoleResourceService {

}
