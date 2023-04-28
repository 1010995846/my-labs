package cn.cidea.module.admin.dataobject.dto;

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
    private Long userId;
    /**
     *
     */
    private Long roleId;
}
