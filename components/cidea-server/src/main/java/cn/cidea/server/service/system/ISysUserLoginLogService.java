package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysUserLoginLog;
import cn.cidea.server.dataobject.enums.LoginTypeEnum;
import cn.cidea.server.dataobject.enums.LoginResultEnum;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录记录(SysUserLoginLog)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Transactional(readOnly = true)
public interface ISysUserLoginLogService {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void fail(String username, LoginTypeEnum logType, LoginResultEnum loginResult, Throwable throwable);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void success(String username, LoginTypeEnum logType);
}
