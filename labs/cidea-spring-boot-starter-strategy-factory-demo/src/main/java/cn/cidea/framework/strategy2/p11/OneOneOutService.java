package cn.cidea.framework.strategy2.p11;

import cn.cidea.framework.strategy.core.annotation.StrategyAPI;
import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy2.IOutService;
import cn.cidea.framework.strategy2.p1.OneOutService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * @author: CIdea
 */
@Slf4j
@StrategyBranch("p1")
public class OneOneOutService extends OneOutService {
    @Override
    public void print() {
        log.info("OneOneOutService");
    }

}
