package cn.cidea.server.service.system;


import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.server.dataobject.entity.SysUserLoginLog;
import cn.cidea.server.dataobject.enums.LoginTypeEnum;
import cn.cidea.server.dataobject.enums.LoginResultEnum;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户登录记录(SysUserLoginLog)表服务接口
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Transactional(readOnly = true)
public interface ISysUserLoginLogService extends IService<SysUserLoginLog> {

    @Transactional
    void create(String username, LoginTypeEnum logTypeEnum, LoginResultEnum badCredentials);
}
