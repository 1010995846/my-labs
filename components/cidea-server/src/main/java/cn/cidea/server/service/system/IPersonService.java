package cn.cidea.server.service.system;


import cn.cidea.server.dataobject.entity.Person;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 个人信息(Person)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:25
 */
@Transactional(readOnly = true)
public interface IPersonService extends IService<Person> {

}
