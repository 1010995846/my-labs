package cn.cidea.server.dataobject.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

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
     * 昵称
     */
    private String nickName;
    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 头像
     */
    private String acatar;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否禁用
     */
    private Integer disabled;
    /**
     * 是否删除
     */
    private Integer deleted;
    /**
     * 电子邮箱
     */
    private String email;
    /**
     * 【次选方案】角色ID集合
     */
    private Object roles;
    /**
     * 实名认证状态：0-未认证；1-已认证；-1-已过期
     */
    private Integer certificated;
    /**
     * 最后登录IP
     */
    private Integer loginIp;
    /**
     * 最后登录时间
     */
    private Date loginTime;
    /**
     * 创建者，可为自身注册
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
}
