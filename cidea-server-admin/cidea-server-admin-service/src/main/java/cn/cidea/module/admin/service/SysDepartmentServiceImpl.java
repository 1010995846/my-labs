package cn.cidea.module.admin.service;

import cn.cidea.module.admin.dal.mysql.ISysDepartmentMapper;
import cn.cidea.module.admin.dataobject.entity.SysDept;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysDepartment)表服务实现类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:26
 */
@Slf4j
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<ISysDepartmentMapper, SysDept> implements ISysDepartmentService {

}
