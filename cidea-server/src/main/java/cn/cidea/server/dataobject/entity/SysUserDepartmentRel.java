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
 * (SysUserDepartmentRel)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:37:59
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class SysUserDepartmentRel extends Model<SysUserDepartmentRel> {

    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 科室ID
     */
    private Long departmentId;

    /**
     * 创建时间
     */
    private Date createTime;

}
