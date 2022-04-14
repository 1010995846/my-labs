package cn.cidea.framework.chain.chain;

/**
 * @author Charlotte
 * （可选）链路节点实现，是否启用该节点
 */
public interface IChain {

    /**
     * 是否启用
     * @return
     */
    default boolean enabled() {
        return true;
    }

}
