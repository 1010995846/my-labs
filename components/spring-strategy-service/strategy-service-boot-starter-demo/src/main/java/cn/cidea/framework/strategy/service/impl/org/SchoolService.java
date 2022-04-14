package cn.cidea.framework.strategy.service.impl.org;

import cn.cidea.framework.strategy.annotation.StrategyBranch;
import cn.cidea.framework.strategy.service.impl.OrgService;
import org.springframework.stereotype.Service;

/**
 * @author Charlotte
 */
@Service
@StrategyBranch("school")
public class SchoolService extends OrgService {

    @Override
    public String getName(String id) {
        return "school";
    }
}
