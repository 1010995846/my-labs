package cn.cidea.module.admin.service.system;

import cn.cidea.module.admin.dal.mysql.ISysUserMapper;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * (SysUser)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<ISysUserMapper, SysUser> implements ISysUserService {

    @Override
    public SysUser getUserByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
    }
}
