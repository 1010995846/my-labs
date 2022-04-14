package cn.cidea.server.controller;


import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.server.dataobject.dto.LoginUserDTO;
import cn.cidea.server.dataobject.dto.SysRoleDTO;
import cn.cidea.server.service.system.ISysRoleService;
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

    @RequestMapping(value = "/save")
    @PreAuthorize("@ps.hasPermission('system:role:save')")
    public Response login(@Valid @RequestBody SysRoleDTO saveDTO){
        Long id = roleService.createRole(saveDTO);
        return Response.success(id);
    }

    @RequestMapping(value = "/disable")
    @PreAuthorize("@ps.hasPermission('system:role:save')")
    public Response authenticateError(Long id, Boolean disabled){
        roleService.updateRoleStatus(id, disabled);
        return Response.success(null);
    }

    @RequestMapping(value = "/delete")
    @PreAuthorize("@ps.hasPermission('system:role:delete')")
    public Response authenticate(Long id){
        roleService.deleteRole(id);
        return Response.success(null);
    }

}
