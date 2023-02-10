package cn.cidea.module.admin.dataobject.dto;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:23
 */
@Data
public class SysTenantDTO implements Serializable {
    /**
     * 租户编号
     */
    private Long id;
    /**
     * 租户名
     */
    private String name;
    /**
     * 联系人的用户编号
     */
    private Long contactUserId;
    /**
     * 租户状态，是否禁用
     */
    private Boolean disabled;
    /**
     * 创建者
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
