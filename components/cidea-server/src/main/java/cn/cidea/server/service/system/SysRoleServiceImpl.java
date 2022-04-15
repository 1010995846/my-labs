package cn.cidea.server.service.system;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.server.dataobject.covert.RoleConvert;
import cn.cidea.server.dataobject.enums.DataScopeEnum;
import cn.cidea.server.framework.security.utils.SecurityFrameworkUtils;
import cn.cidea.server.mq.producer.permission.RoleProducer;
import cn.cidea.server.mybatis.CacheOneServiceImpl;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.cidea.server.dal.mysql.ISysRoleMapper;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import cn.cidea.server.dataobject.enums.RoleCodeEnum;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * (SysRole)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends CacheOneServiceImpl<Long, ISysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    @Resource
    @Lazy
    private ISysPermissionService permissionService;

    @Resource
    private RoleProducer roleProducer;
    @Resource
    private DefaultIdentifierGenerator identifierGenerator;

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        initLocalCache();
    }

    @Override
    @Transactional
    public Long save(SysRoleDTO saveDTO) {
        // 校验角色
        checkDuplicateRole(saveDTO.getName(), saveDTO.getCode(), saveDTO.getId());
        // 插入到数据库
        SysRole role = RoleConvert.INSTANCE.convert(saveDTO);
        boolean isNew = role.getId() == null;
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Date updateTime = new Date();
        if (isNew) {
            role.setId(identifierGenerator.nextId(null));
            role.setBuiltIn(false);
            role.setDisabled(false);
            role.setCreateBy(loginUserId).setCreateTime(updateTime);
        } else {
            // 校验是否可以更新
            checkUpdateRole(role.getId());
        }
        // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        role.setDataScope(DataScopeEnum.ALL.getScope());
        role.setUpdateBy(loginUserId).setUpdateTime(updateTime);
        if (isNew) {
            baseMapper.insert(role);
        } else {
            baseMapper.updateById(role);
        }
        // 发送刷新消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                roleProducer.sendRefreshMessage();
            }
        });
        // 返回
        return role.getId();
    }

    @Override
    public void updateDisabled(Long id, Boolean disabled) {
        // 校验是否可以更新
        checkUpdateRole(id);
        // 更新状态
        SysRole updateObject = new SysRole();
        updateObject.setId(id);
        updateObject.setDisabled(disabled);
        baseMapper.updateById(updateObject);
        // 发送刷新消息
        roleProducer.sendRefreshMessage();
    }

    @Override
    public void updateDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        checkUpdateRole(id);
        // 更新数据范围
        SysRole updateObject = new SysRole();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        // updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        baseMapper.updateById(updateObject);
        // 发送刷新消息
        roleProducer.sendRefreshMessage();
    }

    @Override
    public void delete(Long id) {
        // 校验是否可以更新
        this.checkUpdateRole(id);
        // 标记删除
        baseMapper.deleteById(id);
        // 删除相关数据
        permissionService.processRoleDeleted(id);
        // 发送刷新消息. 注意，需要事务提交后，在进行发送刷新消息。不然 db 还未提交，结果缓存先刷新了
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {

            @Override
            public void afterCommit() {
                roleProducer.sendRefreshMessage();
            }

        });
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<SysRole> roleList) {
        if (CollectionUtil.isEmpty(roleList)) {
            return false;
        }
        return roleList.stream().anyMatch(role -> RoleCodeEnum.isSuperAdmin(role.getCode()));
    }

    /**
     * 校验角色的唯一字段是否重复
     * <p>
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id   角色编号
     */
    public void checkDuplicateRole(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw Assert.FORBIDDEN.build("不允许创建超管");
        }
        // 1. 该 name 名字被其它角色所使用
        SysRole role = baseMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw Assert.VALID.build("名称重复");
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = baseMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw Assert.VALID.build("编码重复");
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    public void checkUpdateRole(Long id) {
        SysRole SysRole = baseMapper.selectById(id);
        if (SysRole == null) {
            throw Assert.VALID.build("ID异常");
        }
        // 内置角色，不允许删除
        if (!Boolean.FALSE.equals(SysRole.getBuiltIn())) {
            throw Assert.VALID.build("不可编辑内置角色");
        }
    }

    @Override
    public void validRoles(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<SysRole> roles = baseMapper.selectBatchIds(ids);
        Map<Long, SysRole> roleMap = roles.stream().collect(Collectors.toMap(SysRole::getId, r -> r));
        // 校验
        ids.forEach(id -> {
            SysRole role = roleMap.get(id);
            if (role == null) {
                throw Assert.VALID.build("角色不存在");
            }
            if (!role.enabled()) {
                throw Assert.VALID.build("角色不可用");
            }
        });
    }
}
