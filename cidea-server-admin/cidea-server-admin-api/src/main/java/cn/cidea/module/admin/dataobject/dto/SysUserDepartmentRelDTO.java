package cn.cidea.module.admin.dataobject.dto;

import java.util.Date;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * (SysUserDepartmentRel)表实体类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:00
 */
@Data
public class SysUserDepartmentRelDTO implements Serializable {

    /**
     * 用户ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 科室ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deptId;
    /**
     * 创建时间
     */
    private Date createTime;
}
