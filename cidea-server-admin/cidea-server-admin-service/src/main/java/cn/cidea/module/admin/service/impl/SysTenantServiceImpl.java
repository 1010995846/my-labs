package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.utils.SecurityFrameworkUtils;
import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.ISysTenantMapper;
import cn.cidea.module.admin.dataobject.convert.SysTenantConvert;
import cn.cidea.module.admin.dataobject.dto.SysTenantSaveDTO;
import cn.cidea.module.admin.dataobject.entity.SysTenant;
import cn.cidea.module.admin.service.ISysTenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表服务实现类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:25
 */
@Slf4j
@Service
public class SysTenantServiceImpl extends ServiceImpl<ISysTenantMapper, SysTenant> implements ISysTenantService {

    @Override
    public List<Long> getTenantIds() {
        List<SysTenant> list = list(new QueryWrapper<SysTenant>().lambda()
                .eq(SysTenant::getDisabled, false)
                .eq(SysTenant::getDeleted, false));
        return list.stream().map(SysTenant::getId).collect(Collectors.toList());
    }

    @Override
    public void validTenant(Long id) {
        SysTenant tenant = getById(id);
        Assert.VALID.nonNull(tenant, "租户不存在!");
        Assert.VALID.isFalse(tenant.getDeleted(), "租户不可用!");
        Assert.VALID.isFalse(tenant.getDisabled(), "租户不可用!");
    }

    @Override
    public SysTenant save(SysTenantSaveDTO saveDTO) {
        SysTenant tenant = SysTenantConvert.INSTANCE.toDO(saveDTO);
        boolean isNew = tenant.getId() == null;
        Date updateTime = new Date();
        LoginUserDTO loginUser = SecurityFrameworkUtils.getAndValidLoginUser();

        tenant.setUpdateBy(loginUser.getId())
                .setUpdateTime(updateTime);
        if(isNew){
            tenant.setId(IdWorker.getId())
                    .setCreateBy(loginUser.getId())
                    .setCreateTime(updateTime);
            save(tenant);
            saveDTO.setId(tenant.getId());
        } else {
            updateById(tenant);
        }
        return tenant;
    }
}
