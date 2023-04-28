package cn.cidea.module.admin.controller;


import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.dataobject.convert.SysRoleConvert;
import cn.cidea.module.admin.dataobject.dto.SysRoleDTO;
import cn.cidea.module.admin.dataobject.entity.SysRole;
import cn.cidea.module.admin.service.ISysRoleService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping(value = "/sys/role")
@Slf4j
public class RoleController {

    @Autowired
    private ISysRoleService roleService;

    @RequestMapping(value = "/list")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public Response list(@Valid @RequestBody SysRoleDTO roleDTO){
        return Response.success(SysRoleConvert.INSTANCE.toDTO(roleService.list()));
    }

    @RequestMapping(value = "/page")
    @PreAuthorize("@ps.hasPermission('system:role:query')")
    public Response page(@Valid @RequestBody SysRoleDTO roleDTO){
        Page<SysRole> page = new Page<>();
        page.setPages(1).setSize(2);
        page = roleService.page(page);
        return Response.success(page.convert(SysRoleConvert.INSTANCE::toDTO));
    }

    @RequestMapping(value = "/save")
    @PreAuthorize("@ps.hasPermission('system:role:save')")
    public Response login(@Valid @RequestBody SysRoleDTO saveDTO){
        Long id = roleService.save(saveDTO);
        return Response.success(id);
    }

    @RequestMapping(value = "/disable")
    @PreAuthorize("@ps.hasPermission('system:role:save')")
    public Response authenticateError(Long id, Boolean disabled){
        roleService.updateDisabled(id, disabled);
        return Response.success(null);
    }

    @RequestMapping(value = "/delete")
    @PreAuthorize("@ps.hasPermission('system:role:delete')")
    public Response authenticate(Long id){
        roleService.delete(id);
        return Response.success(null);
    }

}
