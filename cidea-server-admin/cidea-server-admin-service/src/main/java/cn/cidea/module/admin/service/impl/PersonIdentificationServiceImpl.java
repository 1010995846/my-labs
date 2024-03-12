package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.web.core.asserts.Assert;
import cn.cidea.module.admin.dal.mysql.IPersonIdentificationMapper;
import cn.cidea.module.admin.dataobject.convert.PersonIdentificationConvert;
import cn.cidea.module.admin.dataobject.dto.PersonIdentificationDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import cn.cidea.module.admin.service.IPersonIdentificationService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 个人证件(PersonIdentification)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Slf4j
@Service
public class PersonIdentificationServiceImpl extends ServiceImpl<IPersonIdentificationMapper, PersonIdentification> implements IPersonIdentificationService {

    @Override
    public List<PersonIdentification> certified(Person person, List<PersonIdentificationDTO> identifications) {
        List<PersonIdentification> certified = certified(identifications);
        List<Long> personIds = certified.stream().map(PersonIdentification::getPersonId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        // 单人绑定的各类证件只允许至多一个人
        Assert.BAD_REQUEST.isTrue(personIds.size() <= 1, "不可绑定他人证件。");
        if (person.getId() != null) {
            // 已登记的人绑定的证件不可以是其它人的
            Assert.BAD_REQUEST.isTrue(personIds.size() == 0 || personIds.get(0).equals(person.getId()), "不可绑定他人证件。");
        } else {
            // 未登记的人不可绑定已登记证件
            Assert.BAD_REQUEST.isTrue(personIds.size() == 0, "不可绑定他人证件。");
        }
        if (person.getId() == null && CollectionUtils.isNotEmpty(personIds)) {
            person.setId(personIds.get(0));
        }
        return certified;
    }

    /**
     * 证件登记
     * @param identifications
     * @return
     */
    private List<PersonIdentification> certified(List<PersonIdentificationDTO> identifications) {
        if (CollectionUtils.isEmpty(identifications)) {
            return Collections.emptyList();
        }
        // TODO CIdea: 入参重复校验
        List<PersonIdentification> list = PersonIdentificationConvert.INSTANCE.toEntity(identifications);
        for (PersonIdentification identification : list) {
            PersonIdentification last = baseMapper.selectOne(identification.getType(), identification.getNumber());
            if(last != null){
                identification.setId(last.getId());
                identification.setPersonId(last.getPersonId());
            }
        }
        return list;
    }

    @Override
    public void bind(Person person, List<PersonIdentification> certified) {
        if (CollectionUtils.isEmpty(certified)) {
            return;
        }
        Date updateTime = new Date();
        List<PersonIdentification> updateList = new ArrayList<>();
        List<PersonIdentification> insertList = new ArrayList<>();
        for (PersonIdentification identification : certified) {
            if (identification.getPersonId() == null) {
                identification.setPersonId(person.getId());
            } else {
                Assert.BAD_REQUEST.isTrue(identification.getPersonId().equals(person.getId()), "不可绑定他人证件。");
            }

            identification.setUpdateTime(updateTime);
            if (identification.getId() == null) {
                identification.setId(IdWorker.getId());
                identification.setCreateTime(updateTime);
                insertList.add(identification);
            } else {
                updateList.add(identification);
            }
        }
        updateBatchById(updateList);
        saveBatch(insertList);
    }
}
