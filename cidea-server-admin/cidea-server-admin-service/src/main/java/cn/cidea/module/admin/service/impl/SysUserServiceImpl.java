package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.ISysUserMapper;
import cn.cidea.module.admin.dataobject.convert.SysUserConvert;
import cn.cidea.module.admin.dataobject.dto.SysUserRegisterDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import cn.cidea.module.admin.dataobject.entity.pool.SysUserPool;
import cn.cidea.module.admin.service.IPersonService;
import cn.cidea.module.admin.service.ISysUserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * (SysUser)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<ISysUserMapper, SysUser> implements ISysUserService, UserDetailsService {

    @Autowired
    private IPersonService personService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SysUserPool pool;

    @Override
    public LoginUserDTO loadUserByUsername(String username) {
        SysUser user = getUserByUsername(username);
        Assert.BAD_CREDENTIALS.nonNull(user, "用户名不存在");
        Assert.BAD_CREDENTIALS.isFalse(user.getDisabled(), "账号被禁用");
        Assert.BAD_CREDENTIALS.isFalse(user.getDeleted(), "账号被删除");
        pool.builder(user).role();
        return SysUserConvert.INSTANCE.toLoginDTO(user);
    }

    @Override
    public SysUser getUserByUsername(String username) {
        return baseMapper.selectOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
    }

    @Override
    public SysUser register(SysUserRegisterDTO dto) {
        Person person = personService.trySave(dto.getPerson());
        Assert.BAD_REQUEST.isNull(getUserByUsername(dto.getUsername()), "账号名已存在");

        SysUser user = SysUserConvert.INSTANCE.toEntity(dto);
        boolean isNew = user.getId() == null;
        Date updateTime = new Date();
        if(isNew){
            user.setId(IdWorker.getId());
            user.setCreateTime(updateTime);
        }
        if(StringUtils.isNotBlank(user.getPassword())){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setUpdateTime(updateTime);
        if(person != null){
            user.setPersonId(person.getId());
        }

        if(isNew){
            baseMapper.insert(user);
        } else {
            int update = baseMapper.updateById(user);
            Assert.BAD_REQUEST.isTrue(update == 1, "用户ID异常");
        }
        return user;
    }

}
