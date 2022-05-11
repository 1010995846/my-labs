package cn.cidea.server.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysUserRole)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:30
 */
@Data
@NoArgsConstructor
public class SysUserRoleDTO {
    /**
     *
     */
    private Long userId;
    /**
     *
     */
    private Long roleId;
}
