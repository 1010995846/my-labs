package cn.cidea.framework.common.utils.mq.service;

import com.baomidou.mybatisplus.extension.service.IService;
import cn.cidea.framework.common.utils.mq.entity.SysMessage;

/**
 * @author Charlotte
 */
public interface IMessageService extends IService<SysMessage> {

    /**
     * 发送消息
     * @param context
     */
    void send(String context);

}
