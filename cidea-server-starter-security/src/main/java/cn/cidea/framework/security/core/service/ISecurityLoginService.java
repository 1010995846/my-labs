package cn.cidea.framework.security.core.service;

import cn.cidea.framework.security.core.LoginUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.constraints.NotBlank;

/**
 * 登录相关
 * @author Charlotte
 */
public interface ISecurityLoginService extends UserDetailsService {

    @Override
    LoginUserDTO loadUserByUsername(String username) throws UsernameNotFoundException;

    LoginUserDTO login(@NotBlank String username, @NotBlank String password, String code, String uuid);

}
