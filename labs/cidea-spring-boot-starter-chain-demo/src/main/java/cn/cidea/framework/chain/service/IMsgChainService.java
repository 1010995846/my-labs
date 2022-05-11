package cn.cidea.framework.chain.service;

import cn.cidea.framework.chain.annotation.Chain;

/**
 * @author Charlotte
 */
@Chain
public interface IMsgChainService {

    /**
     * 发送提醒
     */
    void alter();

}
