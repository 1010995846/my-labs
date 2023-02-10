package cn.cidea.module.admin.dataobject.dto;

import cn.cidea.core.masking.DataMasking;
import cn.cidea.core.masking.DataMaskingFunc;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * (SysUser)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@NoArgsConstructor
public class SysUserDTO {
    /**
     *
     */
    private Long id;
    /**
     * 个人信息ID
     */
    private Long personId;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 账号
     */
    @JSONField(name = "DDNAME")
    private String username;
    /**
     * 密码
     */
    @DataMasking(maskFunc = DataMaskingFunc.ALL_MASK)
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
    /**
     * 实名认证状态：0-未认证；1-已认证；-1-已过期
     */
    private Integer certificated;
    /**
     * 是否内置
     */
    private Boolean builtIn;
    /**
     * 是否超管
     */
    private Boolean superAdmin;
    /**
     * 最后登录IP
     */
    private Integer loginIp;
    /**
     * 最后登录时间
     */
    private Date loginTime;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 创建者，可为自身注册
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;

    private List<SysRoleDTO> roles;
}
