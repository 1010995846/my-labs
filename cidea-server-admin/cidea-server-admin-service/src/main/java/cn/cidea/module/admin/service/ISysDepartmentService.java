package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.entity.SysDept;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * (SysDepartment)表服务接口
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:26
 */
@Validated
@Transactional(readOnly = true)
public interface ISysDepartmentService extends IService<SysDept> {

}
