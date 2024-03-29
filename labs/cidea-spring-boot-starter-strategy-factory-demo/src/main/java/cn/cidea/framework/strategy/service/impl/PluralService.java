package cn.cidea.framework.strategy.service.impl;


import cn.cidea.framework.strategy.annotation.P1Branch;
import cn.cidea.framework.strategy.annotation.P2Branch;
import cn.cidea.framework.strategy.service.IOrgService;

/**
 * @author Charlotte
 */
@P1Branch
@P2Branch
//@StrategyBranch("p1")
//@StrategyBranch("p2")
public class PluralService implements IOrgService {
    @Override
    public String getName() {
        return "plural";
    }
}
