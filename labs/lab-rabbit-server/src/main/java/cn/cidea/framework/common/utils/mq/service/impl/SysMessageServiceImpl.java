package cn.cidea.framework.common.utils.mq.service.impl;

import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessage;
import cn.cidea.framework.common.utils.mq.dal.mysql.ISysMessageMapper;
import cn.cidea.framework.common.utils.mq.service.ISysMessageService;
import cn.cidea.framework.mq.redisson.core.RedisMQTemplate;
import cn.cidea.framework.mq.redisson.core.message.AbstractMessage;
import cn.cidea.framework.web.core.asserts.Assert;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.util.ClassUtils;

import javax.annotation.Resource;

/**
 * (SysMessage)表服务实现类
 *
 * @author yechangfei
 * @since 2022-04-27 13:53:04
 */
@Slf4j
@Service
public class SysMessageServiceImpl extends ServiceImpl<ISysMessageMapper, SysMessage> implements ISysMessageService {

    @Resource
    @Lazy
    private RedisMQTemplate mqTemplate;

    @Override
    public void resend(Long id, Boolean retry, JSONObject content) {
        SysMessage message = baseMapper.selectById(id);
        Assert.VALID.nonNull(message, "ID异常");
        Class<?> messageClass;
        try {
            messageClass = this.getClass().getClassLoader().loadClass(message.getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("消息Class加载失败!");
        }
        Assert.VALID.isTrue(ClassUtils.isAssignable(AbstractMessage.class, messageClass), "消息Class异常!");
        if(content == null){
            content = message.getContent();
        }
        AbstractMessage channelMessage = (AbstractMessage) content.toJavaObject(messageClass);
        if(!Boolean.TRUE.equals(retry)){
            channelMessage.setRetry(-1);
        }
        mqTemplate.send(channelMessage);
    }
}
