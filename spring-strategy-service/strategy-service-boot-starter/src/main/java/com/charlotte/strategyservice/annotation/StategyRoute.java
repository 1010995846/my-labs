package com.charlotte.strategyservice.annotation;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author Charlotte
 */
@Component
@Scope("prototype")
public @interface StategyRoute {
}
