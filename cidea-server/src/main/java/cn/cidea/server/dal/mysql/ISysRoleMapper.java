package cn.cidea.server.dal.mysql;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.cidea.server.dataobject.entity.SysRole;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * (SysRole)表数据库访问层
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
public interface ISysRoleMapper extends BaseMapper<SysRole> {

    @Select("SELECT id FROM sys_role WHERE update_time > #{maxUpdateTime} LIMIT 1")
    SysRole selectExistsByUpdateTimeAfter(Date maxUpdateTime);

    @Select("SELECT * FROM sys_role WHERE name = #{name} LIMIT 1")
    SysRole selectByName(String name);

    @Select("SELECT * FROM sys_role WHERE code = #{code} LIMIT 1")
    SysRole selectByCode(String code);
}
