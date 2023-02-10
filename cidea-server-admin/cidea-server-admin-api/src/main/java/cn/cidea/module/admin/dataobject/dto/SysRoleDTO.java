package cn.cidea.module.admin.dataobject.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysRole)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@NoArgsConstructor
public class SysRoleDTO implements Serializable {
    /**
     *
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    private String code;

    /**
     * 是否内置角色
     */
    private Boolean builtIn;
    /**
     * 数据范围
     */
    private Integer dataScope;
    /**
     * 是否禁用
     */
    private Boolean disabled;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;

    private Long tenantId;
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
    /**
     * 是否删除
     */
    private Boolean deleted;
    /**
     * 资源集合
     */
    private List<SysResourceDTO> resources;
}
