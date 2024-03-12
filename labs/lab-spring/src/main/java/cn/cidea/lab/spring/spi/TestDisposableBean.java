package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

/**
 * 触发时机为当此对象销毁时，会自动执行这个方法。比如说运行applicationContext.registerShutdownHook时，就会触发这个方法。
 * @author: CIdea
 */
@Slf4j
@Component
public class TestDisposableBean implements DisposableBean {
    @Override
    public void destroy() throws Exception {

    }
}
