package cn.cidea.server.dal.mysql;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.cidea.server.dataobject.entity.SysTenant;

/**
 * 租户表
 * 类似下游的各个使用商(SysTenant)表数据库访问层
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:24
 */
public interface ISysTenantMapper extends BaseMapper<SysTenant> {
}
