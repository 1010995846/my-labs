package cn.cidea.server.service.system;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.server.dataobject.covert.RoleConvert;
import cn.cidea.server.dataobject.enums.DataScopeEnum;
import cn.cidea.server.mq.producer.permission.RoleProducer;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.cidea.server.dal.mysql.ISysRoleMapper;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import cn.cidea.server.dataobject.enums.RoleCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
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
public class SysRoleServiceImpl extends ServiceImpl<ISysRoleMapper, SysRole> implements ISysRoleService {

    /**
     * 定时执行 {@link #schedulePeriodicRefresh()} 的周期
     * 因为已经通过 Redis Pub/Sub 机制，所以频率不需要高
     */
    private static final long SCHEDULER_PERIOD = 5 * 60 * 1000L;

    /**
     * 角色缓存
     * key：角色编号 {@link SysRole#getId()}
     *
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    private volatile Map<Long, SysRole> roleCache;
    /**
     * 缓存角色的最大更新时间，用于后续的增量轮询，判断是否有更新
     */
    private volatile Date maxUpdateTime;

    @Resource
    @Lazy
    private ISysPermissionService permissionService;
    @Resource
    @Lazy
    private ISysRoleService self;

    @Resource
    private RoleProducer roleProducer;

    /**
     * 初始化 {@link #roleCache} 缓存
     */
    @Override
    @PostConstruct
    public void initLocalCache() {
        // 获取角色列表，如果有更新
        List<SysRole> roleList = loadRoleIfUpdate(maxUpdateTime);
        if (CollUtil.isEmpty(roleList)) {
            return;
        }

        // 写入缓存
        roleCache = roleList.stream().collect(Collectors.toMap(SysRole::getId, r -> r));
        maxUpdateTime = roleList.stream().map(SysRole::getUpdateTime).max(Date::compareTo).get();
        log.info("[initLocalCache][初始化 Role 数量为 {}]", roleList.size());
    }

    @Scheduled(fixedDelay = SCHEDULER_PERIOD, initialDelay = SCHEDULER_PERIOD)
    public void schedulePeriodicRefresh() {
        self.initLocalCache();
    }

    /**
     * 如果角色发生变化，从数据库中获取最新的全量角色。
     * 如果未发生变化，则返回空
     *
     * @param maxUpdateTime 当前角色的最大更新时间
     * @return 角色列表
     */
    private List<SysRole> loadRoleIfUpdate(Date maxUpdateTime) {
        // 第一步，判断是否要更新。
        if (maxUpdateTime == null) {
            // 如果更新时间为空，说明 DB 一定有新数据
            log.info("[loadRoleIfUpdate][首次加载全量角色]");
        } else {
            // 判断数据库中是否有更新的角色
            if (baseMapper.selectExistsByUpdateTimeAfter(maxUpdateTime) == null) {
                return null;
            }
            log.info("[loadRoleIfUpdate][增量加载全量角色]");
        }
        // 第二步，如果有更新，则从数据库加载所有角色
        return baseMapper.selectList(new QueryWrapper<>());
    }

    @Override
    @Transactional
    public Long createRole(SysRoleDTO saveDTO) {
        // 校验角色
        checkDuplicateRole(saveDTO.getName(), saveDTO.getCode(), null);
        // 插入到数据库
        SysRole role = RoleConvert.INSTANCE.convert(saveDTO);
        role.setBuiltIn(false);
        role.setDisabled(false);
        // 默认可查看所有数据。原因是，可能一些项目不需要项目权限
        role.setDataScope(DataScopeEnum.ALL.getScope());
        baseMapper.insert(role);
        // 发送刷新消息
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                roleProducer.sendRoleRefreshMessage();
            }
        });
        // 返回
        return role.getId();
    }

    @Override
    public void updateRole(SysRoleDTO reqVO) {
        // 校验是否可以更新
        checkUpdateRole(reqVO.getId());
        // 校验角色的唯一字段是否重复
        checkDuplicateRole(reqVO.getName(), reqVO.getCode(), reqVO.getId());

        // 更新到数据库
        SysRole updateObject = new SysRole();
        baseMapper.updateById(updateObject);
        // 发送刷新消息
        roleProducer.sendRoleRefreshMessage();
    }

    @Override
    public void updateRoleStatus(Long id, Boolean disabled) {
        // 校验是否可以更新
        checkUpdateRole(id);
        // 更新状态
        SysRole updateObject = new SysRole();
        updateObject.setId(id);
        updateObject.setDisabled(disabled);
        baseMapper.updateById(updateObject);
        // 发送刷新消息
        roleProducer.sendRoleRefreshMessage();
    }

    @Override
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        checkUpdateRole(id);
        // 更新数据范围
        SysRole updateObject = new SysRole();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        // updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        baseMapper.updateById(updateObject);
        // 发送刷新消息
        roleProducer.sendRoleRefreshMessage();
    }

    @Override
    public void deleteRole(Long id) {
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
                roleProducer.sendRoleRefreshMessage();
            }

        });
    }

    @Override
    public SysRole getRoleFromCache(Long id) {
        return roleCache.get(id);
    }

    @Override
    public List<SysRole> listFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleCache.values().stream().filter(SysRole -> ids.contains(SysRole.getId()))
                .collect(Collectors.toList());
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
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
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
            throw Assert.VALID.build("不可删除内置角色");
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
