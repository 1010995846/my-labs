package cn.cidea.module.admin.dataobject.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * (SysResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@NoArgsConstructor
public class SysResourceDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 权限标识
     */
    private List<String> permissions;
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
     * 排序
     */
    private Integer sort;
    /**
     * 创建者
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long createBy;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新者
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long updateBy;
    /**
     * 更新时间
     */
    private Date updateTime;
}
