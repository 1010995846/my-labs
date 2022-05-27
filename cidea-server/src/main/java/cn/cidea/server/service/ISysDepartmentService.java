package cn.cidea.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import cn.cidea.server.dataobject.entity.SysDepartment;
import org.springframework.validation.annotation.Validated;

/**
 * (SysDepartment)表服务接口
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:26
 */
@Validated
@Transactional(readOnly = true)
public interface ISysDepartmentService extends IService<SysDepartment> {

}
