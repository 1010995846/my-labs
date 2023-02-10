package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.entity.SysUserDeptRel;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * (SysUserDepartmentRel)表服务接口
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:22
 */
@Validated
@Transactional(readOnly = true)
public interface ISysUserDepartmentRelService extends IService<SysUserDeptRel> {

}
