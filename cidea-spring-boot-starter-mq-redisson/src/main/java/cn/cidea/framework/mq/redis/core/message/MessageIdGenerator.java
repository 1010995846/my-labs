package cn.cidea.framework.mq.redis.core.message;

/**
 * @author Charlotte
 */
public interface MessageIdGenerator {

    /**
     * next long id
     * @return
     */
    long nextId();

}
