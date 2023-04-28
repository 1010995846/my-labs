package cn.cidea.framework.strategy.core.support;

import cn.cidea.framework.strategy.core.annotation.StrategyAPI;
import cn.cidea.framework.strategy.core.annotation.StrategyBranch;
import cn.cidea.framework.strategy.core.annotation.StrategyBranches;
import cn.cidea.framework.strategy.core.annotation.StrategyMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationConfigurationException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

/**
 * API注册表
 * @author CIdea
 */
public class StrategyRegistry implements InitializingBean, ApplicationContextAware {

    private Logger log = LoggerFactory.getLogger(StrategyRegistry.class);

    private ApplicationContext applicationContext;

    private final Map<Class<?>, Object> apiMasters = new HashMap<>();
    private final Map<Class<?>, Map<String, Object>> apiBranches = new HashMap<>();

    public Object getMaster(Class<?> clz){
        return apiMasters.get(clz);
    }

    public Object getBranch(Class<?> api, String routeKey) {
        return Optional.ofNullable(apiBranches.get(api)).map(m -> m.get(routeKey)).orElse(null);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        registryMaster();
        registryBranch();
    }

    private void registryMaster() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(StrategyMaster.class);
        for (Object bean : beans.values()) {
            // spring注册表会把上级的注解带过来，根据class再校验一次
            if(AnnotationUtils.getAnnotation(AopUtils.getTargetClass(bean), StrategyMaster.class) == null){
                continue;
            }
            Set<Class<?>> apis = getCandidateApis(bean);
            for (Class<?> api : apis) {
                Object exist = apiMasters.put(api, bean);
                if (exist != null) {
                    throw new AnnotationConfigurationException("strategy `" + api.getName() + "` has duplicate master");
                }
            }
        }
    }

    private void registryBranch() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(StrategyBranch.class);
        for (Object bean : beans.values()) {
            Set<Class<?>> apis = getCandidateApis(bean);
            for (Class<?> api : apis) {
                Map<String, Object> branches = apiBranches.get(api);
                List<StrategyBranch> branchList = getBranchAnnotations(AopUtils.getTargetClass(bean));
                if (CollectionUtils.isEmpty(branchList)) {
                    continue;
                }
                if(branches == null){
                    branches = new HashMap<>();
                    apiBranches.put(api, branches);
                }
                for (StrategyBranch branch : branchList) {
                    if (branch.value() == null) {
                        continue;
                    }
                    for (String key : branch.value()) {
                        // 用branch的值注册进去
                        if (branches.containsKey(key)) {
                            log.warn("{} strategy branch key `{}` conflict", api.getName(), key);
                        }
                        branches.put(key, bean);
                    }
                }
            }
        }
    }

    private Set<Class<?>> getCandidateApis(Object bean) {
        Class<?> targetClass = AopUtils.getTargetClass(bean);
        Set<Class<?>> candidateApis = new HashSet<>();
        Class<?> superclass = targetClass;
        while (superclass != null) {
            candidateApis.add(superclass);
            superclass = superclass.getSuperclass();
        }
        candidateApis.addAll(Arrays.asList(ClassUtils.getAllInterfacesForClass(targetClass)));
        Set<Class<?>> apis = candidateApis.stream()
                .filter(candidateApi -> AnnotationUtils.getAnnotation(candidateApi, StrategyAPI.class) != null)
                .collect(Collectors.toSet());
        return apis;
    }

    private List<StrategyBranch> getBranchAnnotations(Class clazz) {
        List<StrategyBranch> strategyBranchList = new ArrayList<>();
        for (Annotation annotation : clazz.getAnnotations()) {
            if (annotation.annotationType().getName().contains("java.lang")) {
                continue;
            } else if (annotation instanceof StrategyBranch) {
                strategyBranchList.add((StrategyBranch) annotation);
            } else if (annotation instanceof StrategyBranches) {
                // 支持repeatable
                strategyBranchList.addAll(Arrays.asList(((StrategyBranches) annotation).value()));
            } else {
                // 支持复合注解
                strategyBranchList.addAll(getBranchAnnotations(annotation.annotationType()));
            }
        }
        return strategyBranchList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
