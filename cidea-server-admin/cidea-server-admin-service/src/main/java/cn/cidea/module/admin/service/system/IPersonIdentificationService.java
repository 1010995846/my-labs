package cn.cidea.module.admin.service.system;


import cn.cidea.module.admin.dataobject.entity.PersonIdentification;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 个人证件(PersonIdentification)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:09:37
 */
@Transactional(readOnly = true)
public interface IPersonIdentificationService extends IService<PersonIdentification> {

}
