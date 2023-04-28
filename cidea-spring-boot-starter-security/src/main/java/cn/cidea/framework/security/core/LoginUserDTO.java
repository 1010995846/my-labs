package cn.cidea.framework.security.core;

import cn.cidea.core.spring.serializer.masking.DataMasking;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * @author Charlotte
 */
@Data
public class LoginUserDTO implements UserDetails {

    private Long id;

    private String sessionId;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    @JsonIgnore
    @DataMasking
    private String password;

    private Integer userType;
    /**
     * 是否超管
     */
    private Boolean superAdmin;

    private Long tenantId;

    /**
     * {@link cn.cidea.module.admin.dataobject.enums.DataScope}
     */
    private Integer dataScope;

    private List<Long> departmentIds;

    private Date loginTime;

    private Long expireTime;

    private Date updateTime;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 是否禁用
     */
    private Boolean disabled;

    private Set<Long> roleIds;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>();
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return Boolean.FALSE.equals(disabled) && Boolean.FALSE.equals(deleted);
    }
}
