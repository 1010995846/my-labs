package com.charlotte.core.mq.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.charlotte.core.mq.entity.SysMessage;
import com.charlotte.core.mq.entity.SysMessageContext;
import com.charlotte.core.mq.mapper.ISysMessageContextMapper;
import com.charlotte.core.mq.mapper.ISysMessageMapper;
import com.charlotte.core.mq.pool.MsgPool;
import com.charlotte.core.mq.service.IMessageService;
import com.charlotte.core.service.utils.SpringContextUtils;
import com.charlotte.core.util.SnowFlake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Charlotte
 */
@Service
@Transactional(rollbackFor = Throwable.class)
public class MessageServiceImpl extends ServiceImpl<ISysMessageMapper, SysMessage> implements IMessageService {

    @Autowired
    private ISysMessageContextMapper contextMapper;

    @Autowired
    private Environment environment;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void send(String context) {
        SysMessage sysMessage = new SysMessage();
        long msgId = SnowFlake.getInstance().nextId();
        sysMessage.setMsgId(msgId);
        try {
            sysMessage.setHost(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sysMessage.setPort(getPort());
        sysMessage.setApplicationName(SpringContextUtils.getApplicationContext().getApplicationName());
        sysMessage.setState("0");
        baseMapper.insert(sysMessage);

        SysMessageContext messageContext = new SysMessageContext();
        messageContext.setMsgId(msgId);
        messageContext.setContext(context);
        sysMessage.setContext(messageContext);
        contextMapper.insert(messageContext);
        publisher.publishEvent(new MsgPool(sysMessage));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void before(MsgPool msgPool){
        msgPool.send();
        return;
    }

    public String getPort(){
        return environment.getProperty("local.server.port");
    }
}
