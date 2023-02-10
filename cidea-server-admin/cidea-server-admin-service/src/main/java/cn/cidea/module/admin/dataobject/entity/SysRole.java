package cn.cidea.module.admin.dataobject.entity;

import java.util.Date;
import java.util.List;

import cn.cidea.framework.mybatisplus.plugin.cache.CacheOneModel;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * (SysRole)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysRole extends CacheOneModel<Long, SysRole> {

    @TableId(value = "id", type = IdType.INPUT)
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
     * 【次选方案】资源ID集合
     */
    // private Object resourceIds;

    @TableField(exist = false)
    private List<SysResource> resources;

    public boolean enabled(){
        return !disabled && !deleted;
    }

}
