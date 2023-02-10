package cn.cidea.framework.strategy.service.impl.org;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.IOrgService;

@StrategyBranch("ext")
public class ExtService implements IOrgService {
    @Override
    public String getName() {
        return "ext";
    }
}
