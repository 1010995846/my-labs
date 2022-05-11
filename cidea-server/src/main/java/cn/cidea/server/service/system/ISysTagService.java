package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysTag;
import org.springframework.transaction.annotation.Transactional;

/**
 * (SysTag)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:27
 */
@Transactional(readOnly = true)
public interface ISysTagService extends IService<SysTag> {

}
