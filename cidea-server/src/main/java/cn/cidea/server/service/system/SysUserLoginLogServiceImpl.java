package cn.cidea.server.service.system;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import cn.cidea.server.dal.mysql.ISysUserLoginLogMapper;
import cn.cidea.server.dataobject.entity.SysUser;
import cn.cidea.server.dataobject.entity.SysUserLoginLog;
import cn.cidea.server.dataobject.enums.LoginTypeEnum;
import cn.cidea.server.dataobject.enums.LoginResultEnum;
import cn.cidea.framework.web.core.utils.ServletUtils;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

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
    private ISysUserService userService;

    @Override
    public void fail(String username, LoginTypeEnum logType, LoginResultEnum loginResult, Throwable ex) {
        create(username, logType, loginResult, ex);
    }

    @Override
    public void success(String username, LoginTypeEnum logType) {
        create(username, logType, LoginResultEnum.SUCCESS, null);
    }

    private void create(String username, LoginTypeEnum logTypeEnum, LoginResultEnum loginResult, Throwable ex) {
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
