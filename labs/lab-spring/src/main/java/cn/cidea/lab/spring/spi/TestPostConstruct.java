package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 在bean的初始化阶段，如果对一个方法标注了@PostConstruct，会先调用这个方法。
 * 这里重点是要关注下这个标准的触发点，这个触发点是在postProcessBeforeInitialization实例化之后，InitializingBean.afterPropertiesSet之前。
 * @author: CIdea
 */
@Slf4j
@Component
public class TestPostConstruct {

    @PostConstruct
    public void init() {
        log.info("Custom PostConstruct");
    }

}
