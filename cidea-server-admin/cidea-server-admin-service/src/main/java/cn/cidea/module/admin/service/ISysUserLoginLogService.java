package cn.cidea.module.admin.service;


import cn.cidea.module.admin.dataobject.enums.LoginResult;
import cn.cidea.module.admin.dataobject.enums.LoginType;
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
    void fail(String username, LoginType logType, LoginResult loginResult, Throwable throwable);

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    void success(String username, LoginType logType);
}
