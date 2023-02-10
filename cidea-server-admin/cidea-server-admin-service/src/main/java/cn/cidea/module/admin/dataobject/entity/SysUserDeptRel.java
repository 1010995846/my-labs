package cn.cidea.module.admin.dataobject.entity;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
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
public class SysUserDeptRel extends Model<SysUserDeptRel> {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 科室ID
     */
    private Long deptId;

    /**
     * 创建时间
     */
    private Date createTime;

}
