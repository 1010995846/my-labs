package cn.cidea.module.admin.dataobject.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * (SysUser)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUser extends Model<SysUser> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 个人信息ID
     */
    private Long personId;
    @TableField(exist = false)
    private Person person;

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
    private String username;
    /**
     * 密码
     */
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
    private Date updateTime;

    @TableField(exist = false)
    private List<SysRole> roles;

}
