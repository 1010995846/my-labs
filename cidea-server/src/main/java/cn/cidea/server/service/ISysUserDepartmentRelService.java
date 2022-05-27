package cn.cidea.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;
import cn.cidea.server.dataobject.entity.SysUserDepartmentRel;
import org.springframework.validation.annotation.Validated;

/**
 * (SysUserDepartmentRel)表服务接口
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:22
 */
@Validated
@Transactional(readOnly = true)
public interface ISysUserDepartmentRelService extends IService<SysUserDepartmentRel> {

}
