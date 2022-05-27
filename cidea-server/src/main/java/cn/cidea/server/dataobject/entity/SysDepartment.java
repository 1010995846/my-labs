package cn.cidea.server.dataobject.entity;

import java.util.Date;

import lombok.Data;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * (SysDepartment)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysDepartment extends Model<SysDepartment> {

    @TableId(value = "id", type = IdType.INPUT)
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
