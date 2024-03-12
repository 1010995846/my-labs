package cn.cidea.framework.strategy.core.annotation;

import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 策略主干（唯一）
 * 未匹配到分支{@link StrategyBranch}时选择主干实现，若无实现（不建议）则抛出异常
 * @author CIdea
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Service
public @interface StrategyMaster {

}
