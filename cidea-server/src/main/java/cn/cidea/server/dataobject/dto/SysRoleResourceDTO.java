package cn.cidea.server.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (SysRoleResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@NoArgsConstructor
public class SysRoleResourceDTO {
    /**
     *
     */
    private Long roleId;
    /**
     *
     */
    private Long resourceId;
}
