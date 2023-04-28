package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.dto.PersonIdentificationDTO;
import cn.cidea.module.admin.dataobject.entity.Person;
import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 个人证件(PersonIdentification)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Transactional(readOnly = true)
public interface IPersonIdentificationService extends IService<PersonIdentification> {

    boolean certified(Person person, List<PersonIdentificationDTO> identifications);
}
