package cn.cidea.server.service;

import cn.cidea.server.dal.mysql.ISysDepartmentMapper;
import cn.cidea.server.dataobject.entity.SysDepartment;
import cn.cidea.server.service.ISysDepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysDepartment)表服务实现类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:26
 */
@Slf4j
@Service
public class SysDepartmentServiceImpl extends ServiceImpl<ISysDepartmentMapper, SysDepartment> implements ISysDepartmentService {

}
