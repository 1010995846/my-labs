package cn.cidea.server.dataobject.dto;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@NoArgsConstructor
public class SysResourceDTO {
    /**
     *
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 权限标识
     */
    private Object perms;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 类型：C-目录；M-菜单；B-按钮
     */
    private String type;
    /**
     * 上级ID
     */
    private Integer parentId;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 是否可见
     */
    private Boolean visible;
    /**
     * 图标
     */
    private String icon;
    /**
     * 备注
     */
    private String remark;
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