package cn.cidea.server.service.system;

import cn.cidea.server.dal.mysql.ISysTagMapper;
import cn.cidea.server.dataobject.entity.SysTag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * (SysTag)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:28
 */
@Slf4j
@Service
public class SysTagServiceImpl extends ServiceImpl<ISysTagMapper, SysTag> implements ISysTagService {

}
