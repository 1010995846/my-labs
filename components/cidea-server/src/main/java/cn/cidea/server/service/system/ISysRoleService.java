package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * (SysRole)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Validated
@Transactional(readOnly = true)
public interface ISysRoleService extends IService<SysRole> {

    void initLocalCache();

    @Transactional
    Long createRole(@Valid SysRoleDTO saveDTO);

    @Transactional
    void updateRole(@Valid SysRoleDTO reqVO);

    @Transactional
    void updateRoleStatus(@NotNull Long id, Boolean disabled);

    @Transactional
    void updateRoleDataScope(@NotNull Long id, @NotNull Integer dataScope, Set<Long> dataScopeDeptIds);

    @Transactional
    void deleteRole(@NotNull Long id);

    SysRole getRoleFromCache(Long id);

    /**
     * 获得角色数组，从缓存中
     * @param ids 角色编号数组
     * @return 角色数组
     */
    List<SysRole> listFromCache(Collection<Long> ids);

    void validRoles(Collection<Long> ids);

    default boolean hasAnySuperAdmin(Set<Long> ids) {
        return hasAnySuperAdmin(listFromCache(ids));
    }

    boolean hasAnySuperAdmin(Collection<SysRole> roleList);
}
