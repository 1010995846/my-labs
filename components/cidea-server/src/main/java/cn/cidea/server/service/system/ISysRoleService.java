package cn.cidea.server.service.system;


import cn.cidea.server.mybatis.ICacheOneService;
import cn.cidea.server.mybatis.ICacheService;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.dataobject.entity.SysRole;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
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
public interface ISysRoleService extends IService<SysRole>, ICacheOneService<Long, SysRole> {

    @Transactional
    Long save(@Valid SysRoleDTO saveDTO);

    @Transactional
    void updateDisabled(@NotNull Long id, Boolean disabled);

    @Transactional
    void updateDataScope(@NotNull Long id, @NotNull Integer dataScope, Set<Long> dataScopeDeptIds);

    @Transactional
    void delete(@NotNull Long id);

    void validRoles(Collection<Long> ids);

    default boolean hasAnySuperAdmin(Set<Long> ids) {
        return hasAnySuperAdmin(listFromCache(ids));
    }

    boolean hasAnySuperAdmin(Collection<SysRole> roleList);
}
