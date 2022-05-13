package cn.cidea.framework.strategy.core;

import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Charlotte
 */
public interface IStrategyRoute {

    String[] getRouteKeys(Object obj, Method method, Object[] args, MethodProxy methodProxy);

}
