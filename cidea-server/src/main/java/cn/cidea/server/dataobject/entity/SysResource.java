package cn.cidea.server.dataobject.entity;

import java.util.Date;
import java.util.List;

import cn.cidea.server.mybatis.CacheModel;
import cn.cidea.server.mybatis.CacheOneModel;
import cn.cidea.server.mybatis.handlers.FastjsonNullDefaultTypeHandler;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

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