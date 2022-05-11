package cn.cidea.server.service.auth;

import cn.cidea.server.dataobject.dto.LoginUserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.validation.constraints.NotBlank;

/**
 * @author Charlotte
 */
public interface ILoginService extends UserDetailsService {

    @Override
    LoginUserDTO loadUserByUsername(String username) throws UsernameNotFoundException;

    LoginUserDTO login(@NotBlank String username, @NotBlank String password, String code, String uuid);

    LoginUserDTO verifySessionAndRefresh(String sessionId);

    LoginUserDTO mockLogin(Long userId);
}
