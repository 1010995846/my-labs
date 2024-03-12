package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.dto.PersonSaveDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

/**
 * 个人信息(Person)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Validated
@Transactional(readOnly = true)
public interface IPersonService extends IService<Person> {

    @Transactional
    Person save(@Valid PersonSaveDTO person);

}
