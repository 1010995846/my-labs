package cn.cidea.server.dataobject.entity;

import cn.cidea.server.mybatis.CacheModel;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

import java.util.Date;

/**
 * (SysRoleResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysRoleResource extends CacheModel<SysRoleResource> {

    private Long roleId;

    private Long resourceId;

    private Long updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    private Boolean deleted;
}
