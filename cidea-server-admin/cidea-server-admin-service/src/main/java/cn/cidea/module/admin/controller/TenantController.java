package cn.cidea.module.admin.controller;


import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.framework.security.core.aspect.Tenant;
import cn.cidea.framework.web.core.api.Request;
import cn.cidea.framework.web.core.api.Response;
import cn.cidea.module.admin.dataobject.convert.SysTenantConvert;
import cn.cidea.module.admin.dataobject.dto.SysTenantSaveDTO;
import cn.cidea.module.admin.dataobject.entity.SysTenant;
import cn.cidea.module.admin.service.ISysTenantService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping(value = "/sys/tenant")
@Slf4j
public class TenantController {

    @Autowired
    private ISysTenantService tenantService;

    @RequestMapping(value = "/list")
    @PreAuthorize("@ps.superAdmin()")
    @Tenant
    public Response list(){
        List<SysTenant> list = tenantService.list(new QueryWrapper<SysTenant>().lambda()
                .eq(SysTenant::getDeleted, false));
        return Response.success(SysTenantConvert.INSTANCE.toDTO(list));
    }

    @RequestMapping(value = "/save")
    @PreAuthorize("@ps.superAdmin()")
    public Response save(@RequestBody SysTenantSaveDTO saveDTO){
        SysTenant tenant = tenantService.save(saveDTO);
        return Response.success(SysTenantConvert.INSTANCE.toDTO(tenant));
    }

}
