package cn.cidea.module.admin.dataobject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * (SysRoleResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@NoArgsConstructor
public class SysRoleResourceDTO implements Serializable {
    /**
     *
     */
    private Long roleId;
    /**
     *
     */
    private Long resourceId;
}
