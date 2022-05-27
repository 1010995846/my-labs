package cn.cidea.server.service;

import cn.cidea.server.dal.mysql.ISysUserDepartmentRelMapper;
import cn.cidea.server.dataobject.entity.SysUserDepartmentRel;
import cn.cidea.server.service.ISysUserDepartmentRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysUserDepartmentRel)表服务实现类
 *
 * @author yechangfei
 * @since 2022-05-19 16:38:23
 */
@Slf4j
@Service
public class SysUserDepartmentRelServiceImpl extends ServiceImpl<ISysUserDepartmentRelMapper, SysUserDepartmentRel> implements ISysUserDepartmentRelService {

}
