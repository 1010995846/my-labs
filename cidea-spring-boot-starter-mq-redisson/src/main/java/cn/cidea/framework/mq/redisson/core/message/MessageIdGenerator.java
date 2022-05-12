package cn.cidea.framework.mq.redisson.core.message;

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
