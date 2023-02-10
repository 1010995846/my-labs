package cn.cidea.module.admin.dataobject.entity;

import cn.cidea.framework.mybatisplus.plugin.cache.CacheOneModel;
import cn.cidea.framework.mybatisplus.plugin.handlers.FastjsonNullDefaultTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * (SysResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:26
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@TableName(autoResultMap = true)
public class SysResource extends CacheOneModel<Long, SysResource> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 权限标识
     */
    @TableField(typeHandler = FastjsonNullDefaultTypeHandler.class)
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
    private Long parentId;
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
