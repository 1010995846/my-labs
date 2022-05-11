package cn.cidea.server.dal.mysql;


import cn.cidea.server.dataobject.entity.SysResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * (SysResource)表数据库访问层
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
public interface ISysResourceMapper extends BaseMapper<SysResource> {

    @Select("SELECT id FROM system_menu WHERE update_time > #{maxUpdateTime} LIMIT 1")
    SysResource selectExistsByUpdateTimeAfter(Date maxUpdateTime);
}
