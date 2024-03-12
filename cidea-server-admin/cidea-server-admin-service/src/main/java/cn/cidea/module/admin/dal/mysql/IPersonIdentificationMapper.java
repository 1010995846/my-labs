package cn.cidea.module.admin.dal.mysql;


import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * 个人证件(PersonIdentification)表数据库访问层
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
public interface IPersonIdentificationMapper extends BaseMapper<PersonIdentification> {

    PersonIdentification selectOne(Integer type, String number);
}
