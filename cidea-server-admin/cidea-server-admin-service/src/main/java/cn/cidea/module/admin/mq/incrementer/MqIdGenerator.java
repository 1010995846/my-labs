package cn.cidea.module.admin.mq.incrementer;

import cn.cidea.framework.mq.redisson.core.message.MessageIdGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Component;

@Component
public class MqIdGenerator implements MessageIdGenerator {

    @Override
    public long nextId() {
        return IdWorker.getId();
    }
}
