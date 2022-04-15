package cn.cidea.framework.chain.chain;

/**
 * （可选）添加链路节点实现是否启用的配置
 * @author Charlotte
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
