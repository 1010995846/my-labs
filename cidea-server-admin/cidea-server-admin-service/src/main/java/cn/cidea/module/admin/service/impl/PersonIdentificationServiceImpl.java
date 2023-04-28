package cn.cidea.module.admin.service.impl;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 个人证件(PersonIdentification)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Slf4j
@Service
public class PersonIdentificationServiceImpl extends ServiceImpl<IPersonIdentificationMapper, PersonIdentification> implements IPersonIdentificationService {

    public boolean certified(Person person, List<PersonIdentificationDTO> identifications){
        if(CollectionUtils.isEmpty(identifications)){
            return false;
        }
        Date updateTime = new Date();
        List<PersonIdentification> list = PersonIdentificationConvert.INSTANCE.toEntity(identifications);
        List<PersonIdentification> insertList = new ArrayList<>();
        List<PersonIdentification> updateList = new ArrayList<>();
        for (PersonIdentification identification : list) {
            identification.setPersonId(person.getId());
            identification.setCreateTime(updateTime);
            if(identification.getId() == null){
                identification.setId(IdWorker.getId());
                identification.setCreateTime(updateTime);
                insertList.add(identification);
            } else {
                updateList.add(identification);
            }
        }
        saveBatch(insertList);
        updateBatchById(updateList);
        return true;
    }
}
