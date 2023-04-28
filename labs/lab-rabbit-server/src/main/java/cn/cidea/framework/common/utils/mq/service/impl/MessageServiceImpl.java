package cn.cidea.framework.common.utils.mq.service.impl;

import cn.cidea.framework.common.utils.mq.service.IMessageService;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessage;
import cn.cidea.framework.common.utils.mq.dataobject.entity.SysMessageContext;
import cn.cidea.framework.common.utils.mq.dal.mysql.ISysMessageContextMapper;
import cn.cidea.framework.common.utils.mq.dal.mysql.ISysMessageMapper;
import cn.cidea.framework.common.utils.mq.dataobject.entity.pool.MsgBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void send(String context) {
        SysMessage sysMessage = new SysMessage();
        long msgId = IdWorker.getId();
        sysMessage.setMsgId(msgId);
        try {
            sysMessage.setHost(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        sysMessage.setPort(getPort());
        sysMessage.setApplicationName(applicationContext.getApplicationName());
        sysMessage.setState("0");
        baseMapper.insert(sysMessage);

        SysMessageContext messageContext = new SysMessageContext();
        messageContext.setMsgId(msgId);
        messageContext.setContext(context);
        sysMessage.setContext(messageContext);
        contextMapper.insert(messageContext);
        publisher.publishEvent(new MsgBuilder(sysMessage));
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void before(MsgBuilder msgBuilder){
        msgBuilder.send();
        return;
    }

    public String getPort(){
        return environment.getProperty("local.server.port");
    }
}
