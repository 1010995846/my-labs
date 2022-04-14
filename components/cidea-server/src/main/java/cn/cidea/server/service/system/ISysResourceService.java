package cn.cidea.server.service.system;


import cn.cidea.server.dataobject.entity.SysResource;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * (SysResource)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Transactional(readOnly = true)
public interface ISysResourceService extends IService<SysResource> {

    void initLocalCache();

    List<SysResource> listByPermissionFromCache(String permission);
}
