package cn.cidea.server.dataobject.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:23
 */
@Data
public class SysTenantSaveDTO implements Serializable {
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

}
