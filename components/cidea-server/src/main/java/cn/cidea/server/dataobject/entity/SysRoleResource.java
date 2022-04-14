package cn.cidea.server.dataobject.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

/**
 * (SysRoleResource)表实体类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Data
@Accessors(chain = true)
public class SysRoleResource {

    private Long roleId;

    private Long resourceId;
}
