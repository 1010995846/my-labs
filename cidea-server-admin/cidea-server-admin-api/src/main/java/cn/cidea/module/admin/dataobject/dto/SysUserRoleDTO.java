package cn.cidea.module.admin.dataobject.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysUserRole)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:30
 */
@Data
@NoArgsConstructor
public class SysUserRoleDTO implements Serializable {
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;
    /**
     *
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;
}
