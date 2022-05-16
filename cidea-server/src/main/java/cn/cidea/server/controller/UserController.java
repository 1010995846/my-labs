package cn.cidea.server.controller;


import cn.cidea.framework.web.core.api.Response;
import cn.cidea.framework.security.core.LoginUserDTO;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.framework.security.core.service.ISecurityLoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Charlotte
 */
@RestController
@RequestMapping(value = "/sys/user")
@Slf4j
public class UserController {

    @Autowired
    private ISecurityLoginService loginService;

    @RequestMapping(value = "/login")
    public Response login(String username, String password, String code, String uuid){
        LoginUserDTO loginDTO = loginService.login(username, password, code, uuid);
        return Response.success(loginDTO);
    }

    /**
     * 权限认证测试
     * @return
     */
    @RequestMapping(value = "/authenticate")
    @PreAuthorize("@ps.hasPermission('system:dict:query')")
    public Response authenticate(){
        return Response.success(null);
    }

    @RequestMapping(value = "/authenticate/error")
    @PreAuthorize("@ps.hasPermission('system:dict:error')")
    public Response authenticateError(){
        return Response.success(null);
    }
    
    @RequestMapping(value = "/register")
    @ResponseBody
    public void register(@RequestBody SysUser user){
        return;
    }

}
