package cn.cidea.lab.spring.security.controller;

import cn.cidea.lab.spring.security.config.SecurityConfig;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * {@link SecurityConfig#configure(HttpSecurity)}
 * @author Charlotte
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/echo")
    public String demo() {
        // permitAll()，直接访问，无需登录
        return "示例返回";
    }

    @GetMapping("/home")
    public String home() {
        // authenticated()，无法直接访问，需要进行登录
        return "我是首页";
    }

    @GetMapping("/admin")
    public String admin() {
        // hasRole("ADMIN")，需要 ADMIN 角色
        return "我是管理员";
    }

    @GetMapping("/normal")
    public String normal() {
        // hasRole('ROLE_NORMAL')，需要 NORMAL 角色
        return "我是普通用户";
    }

}
