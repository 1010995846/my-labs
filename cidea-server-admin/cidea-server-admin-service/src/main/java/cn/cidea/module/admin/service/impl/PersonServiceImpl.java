package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.IPersonMapper;
import cn.cidea.module.admin.dataobject.convert.PersonConvert;
import cn.cidea.module.admin.dataobject.dto.PersonSaveDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import cn.cidea.module.admin.service.IPersonIdentificationService;
import cn.cidea.module.admin.service.IPersonService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 个人信息(Person)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Slf4j
@Service
public class PersonServiceImpl extends ServiceImpl<IPersonMapper, Person> implements IPersonService {

    @Autowired
    private IPersonIdentificationService identificationService;

    @Override
    public Person save(PersonSaveDTO dto) {
        Person person = PersonConvert.INSTANCE.toEntity(dto);
        List<PersonIdentification> certified = identificationService.certified(person, dto.getIdentifications());

        boolean isNew = person.getId() == null;
        Date updateTime = new Date();
        person.setUpdateTime(updateTime);
        if (isNew){
            person.setId(IdWorker.getId());
            person.setCreateTime(updateTime);
            baseMapper.insert(person);
        } else {
            int update = baseMapper.updateById(person);
            Assert.BAD_REQUEST.isTrue(update == 1, "ID异常");
        }
        identificationService.bind(person, certified);
        return person;
    }

}
