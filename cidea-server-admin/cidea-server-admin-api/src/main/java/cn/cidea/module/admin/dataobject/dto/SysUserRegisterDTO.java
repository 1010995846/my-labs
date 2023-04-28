package cn.cidea.module.admin.dataobject.dto;

import cn.cidea.core.spring.serializer.masking.DataMasking;
import cn.cidea.core.spring.serializer.masking.DataMaskingFunc;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 注册
 */
@Data
@NoArgsConstructor
public class SysUserRegisterDTO implements Serializable {
    /**
     * 个人信息ID
     */
    @NotNull
    @Valid
    private PersonSaveDTO person;

    /**
     * 昵称
     */
    @NotBlank
    private String nickName;
    /**
     * 账号
     */
    @NotBlank
    private String username;
    /**
     * 密码
     */
    @NotBlank
    private String password;
    /**
     * 手机号
     */
    private String tel;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 电子邮箱
     */
    private String email;

}
