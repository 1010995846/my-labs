package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.IPersonMapper;
import cn.cidea.module.admin.dataobject.convert.PersonConvert;
import cn.cidea.module.admin.dataobject.dto.PersonSaveDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
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
    public Person trySave(PersonSaveDTO dto) {
        Person person = PersonConvert.INSTANCE.toEntity(dto);
        Person last = getOne(person);
        if(last != null){
            if(person.getId() != null){
                Assert.BAD_REQUEST.isTrue(person.getId().equals(last.getId()), "个人信息更新ID异常，已存在");
            }
            person.setId(last.getId());
        }

        boolean isNew = person.getId() == null;
        Date updateTime = new Date();
        if(isNew){
            person.setId(IdWorker.getId());
            person.setCreateTime(updateTime);
        }
        boolean certified = identificationService.certified(person, dto.getIdentifications());
        if(certified){
            person.setCertified(true);
        }
        person.setUpdateTime(updateTime);
        if (isNew){
            baseMapper.insert(person);
        } else {
            int update = baseMapper.updateById(person);
            Assert.BAD_REQUEST.isTrue(update == 1, "ID异常");
        }
        return person;
    }

    private Person getOne(Person person){
        List<Person> list = list(new QueryWrapper<Person>().lambda()
                .eq(Person::getName, person.getName())
                .orderByDesc(Person::getCreateTime));
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        if(list.size() > 1){
            log.warn("[个人]姓名重复。name = {}", person.getName());
        }
        return list.get(0);
    }

}
