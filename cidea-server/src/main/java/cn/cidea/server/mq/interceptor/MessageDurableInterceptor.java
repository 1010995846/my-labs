package cn.cidea.server.mq.interceptor;

import cn.cidea.framework.mq.redis.core.interceptor.MessageInterceptor;
import cn.cidea.framework.mq.redis.core.message.AbstractMessage;
import cn.cidea.server.dal.redis.MessageRedisDAO;
import cn.cidea.server.dataobject.entity.SysMessage;
import cn.cidea.server.service.ISysMessageService;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Charlotte
 */
@Slf4j
@Component
public class MessageDurableInterceptor implements MessageInterceptor {

    @Autowired
    private MessageRedisDAO redisDAO;

    private static final String HEADER_ID = "uniqueId";

    @Override
    public void sendBefore(AbstractMessage message) {
        message.addHeader(HEADER_ID, IdWorker.getIdStr());
        save(message, true, null);
    }
    @Override
    public void consumeSuccess(AbstractMessage message) {
        save(message, false, null);
    }

    @Override
    public void consumeError(AbstractMessage message, RuntimeException e) {
        save(message, false, e);
    }

    private void save(AbstractMessage message, boolean isNew, RuntimeException e) {
        String uniqueId = message.getHeader(HEADER_ID);
        SysMessage sysMessage = new SysMessage();
        sysMessage.setId(Long.valueOf(uniqueId))
                .setChannel(message.getChannel().toString())
                .setMsgId(message.getId())
                .setRetry(message.getRetry())
                .setContent(JSONObject.parseObject(JSONObject.toJSONString(message)))
                .setClassName(message.getClass().getName())
                .setCreateTime(message.getSendTime())
                .setUpdateTime(new Date());
        if(isNew){
            sysMessage.setStatus(0)
                    .setAck(false);
        } else {
            sysMessage.setStatus(1)
                    .setAck(true);
        }
        if(message.getNextDuration() != null){
            sysMessage.setNextRetry(message.getNextDuration().toMillis());
        }
        if(e != null){
            sysMessage.setStatus(-1)
                    .setErrorMsg(e.getMessage());
        }

        if(isNew){
            // messageService.save(sysMessage);
            redisDAO.write(sysMessage);
        } else {
            // messageService.updateById(sysMessage);
            redisDAO.write(sysMessage);
        }
    }

    @Override
    public void consumeFinally(AbstractMessage message) {
    }
}
