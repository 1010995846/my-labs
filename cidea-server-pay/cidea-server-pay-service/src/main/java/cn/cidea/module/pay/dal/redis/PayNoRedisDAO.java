package cn.cidea.module.pay.dal.redis;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 支付序号的 Redis DAO
 *
 * @author 芋道源码
 */
@Repository
public class PayNoRedisDAO {

    @Resource
    private RedissonClient redissonClient;

    /**
     * 生成序号
     *
     * @param prefix 前缀
     * @return 序号
     */
    public String generate(String prefix) {
        String noPrefix = prefix + DateUtil.format(LocalDateTime.now(), DatePattern.PURE_DATETIME_PATTERN);
        Long no = redissonClient.getIdGenerator(noPrefix).nextId();
        return noPrefix + no;
    }

}
