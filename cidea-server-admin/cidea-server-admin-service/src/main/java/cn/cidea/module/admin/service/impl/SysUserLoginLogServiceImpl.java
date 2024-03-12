package cn.cidea.module.admin.service.impl;

import cn.cidea.framework.web.core.utils.ServletUtils;
import cn.cidea.module.admin.dal.mysql.ISysUserLoginLogMapper;
import cn.cidea.module.admin.dataobject.entity.SysUser;
import cn.cidea.module.admin.dataobject.entity.SysUserLoginLog;
import cn.cidea.module.admin.dataobject.enums.LoginResult;
import cn.cidea.module.admin.dataobject.enums.LoginType;
import cn.cidea.module.admin.service.ISysUserLoginLogService;
import cn.cidea.module.admin.service.ISysUserService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户登录记录(SysUserLoginLog)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-06 18:06:29
 */
@Slf4j
@Service
public class SysUserLoginLogServiceImpl extends ServiceImpl<ISysUserLoginLogMapper, SysUserLoginLog> implements ISysUserLoginLogService {

    @Autowired
    @Lazy
    private ISysUserService userService;

    @Override
    public void fail(String username, LoginType logType, LoginResult loginResult, Throwable ex) {
        create(username, logType, loginResult, ex);
    }

    @Override
    public void success(String username, LoginType logType) {
        create(username, logType, LoginResult.SUCCESS, null);
    }

    private void create(String username, LoginType logTypeEnum, LoginResult loginResult, Throwable ex) {
        // 查询用户
        SysUser user = userService.getUserByUsername(username);
        // 插入登录日志
        SysUserLoginLog loginLog = new SysUserLoginLog();
        loginLog.setId(IdWorker.getId());
        loginLog.setType(logTypeEnum.getType());
        if (user != null) {
            loginLog.setUserId(user.getId());
        }
        loginLog.setUsername(username);
        loginLog.setUserAgent(ServletUtils.getUserAgent());
        String clientIP = ServletUtils.getClientIP();
        loginLog.setIp(1);
        loginLog.setTime(new Date());
        loginLog.setResult(loginResult.getResult());
        if(ex != null){
            loginLog.setRemark(ex.getMessage());
        }
        baseMapper.insert(loginLog);
        // 更新最后登录时间
        // if (user != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
        //     userService.updateUserLogin(user.getId(), clientIP);
        // }
    }
}
