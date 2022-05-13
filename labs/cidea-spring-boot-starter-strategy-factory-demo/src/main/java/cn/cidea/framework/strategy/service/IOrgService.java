package cn.cidea.framework.strategy.service;

import cn.cidea.framework.strategy.core.annotation.Strategy;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Charlotte
 */
@Strategy
public interface IOrgService {

    String getName(String id);

}
