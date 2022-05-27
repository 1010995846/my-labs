package cn.cidea.server.dataobject.dto;

import java.util.Date;
import java.io.Serializable;

import lombok.Data;

/**
 * (SysDepartment)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:25
 */
@Data
public class SysDepartmentDTO implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 上级部门ID
     */
    private Long parentId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
