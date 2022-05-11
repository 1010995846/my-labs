package cn.cidea.server.service.system;

import cn.cidea.server.dal.mysql.IPersonIdentificationMapper;
import cn.cidea.server.dataobject.entity.PersonIdentification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * 个人证件(PersonIdentification)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Slf4j
@Service
public class PersonIdentificationServiceImpl extends ServiceImpl<IPersonIdentificationMapper, PersonIdentification> implements IPersonIdentificationService {

}
