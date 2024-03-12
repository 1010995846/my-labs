package cn.cidea.lab.utils.modules.idempotent.token;

import org.redisson.api.RSetCache;
import org.redisson.api.RedissonClient;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author: CIdea
 */
public class RedisIdempotentStorage  implements IdempotentStorage {

    @Resource
    private RedissonClient redissonClient;

    @Override
    public void save(String id) {
        getCache().add(id, 10, TimeUnit.MINUTES);
    }

    @Override
    public boolean delete(String id) {
        return getCache().remove(id);
    }

    private RSetCache<Object> getCache() {
        return redissonClient.getSetCache("request:idempotent");
    }
}