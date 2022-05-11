package cn.cidea.framework.chain.service.impl;

import cn.cidea.framework.chain.entity.SysLog;
import cn.cidea.framework.chain.mapper.ISysLogMapper;
import com.alibaba.fastjson.JSONObject;
import cn.cidea.framework.chain.chain.IChainLogService;
import cn.cidea.framework.chain.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Date;

@Service
public class SysLogServiceImpl implements ISysLogService, IChainLogService {

    @Autowired
    private ISysLogMapper baseMapper;

    @Override
    public void success(String beanName, Method method, Object[] args, Object ret) {
        SysLog sysLog = build(beanName, method, args);
        sysLog.setSuccess(true);
        if(ret != null){
            sysLog.setMsg(JSONObject.toJSONString(ret));
        }
        baseMapper.insert(sysLog);
    }

    @Override
    public void fail(String beanName, Method method, Object[] args, Throwable exception) {
        SysLog sysLog = build(beanName, method, args);
        sysLog.setSuccess(false);
        sysLog.setMsg(exception.toString());
        baseMapper.insert(sysLog);
    }

    private SysLog build(String beanName, Method method, Object[] args) {
        SysLog sysLog = new SysLog();
        sysLog.setTarget(beanName)
                .setMethod(method.getName())
                .setCreateTime(new Date());
        if (args != null) {
            sysLog.setArgs(JSONObject.toJSONString(args));
        }
        return sysLog;
    }
}
