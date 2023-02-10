package cn.cidea.module.admin.service.system;

import cn.cidea.module.admin.dal.mysql.IPersonMapper;
import cn.cidea.module.admin.dataobject.entity.Person;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 个人信息(Person)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Slf4j
@Service
public class PersonServiceImpl extends ServiceImpl<IPersonMapper, Person> implements IPersonService {

}
