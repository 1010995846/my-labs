package cn.cidea.lab.spring.spi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * ApplicationListener可以监听某个事件的event，触发时机可以穿插在业务方法执行过程中，用户可以自定义某个业务事件。
 * 但是spring内部也有一些内置事件，这种事件，可以穿插在启动调用中。我们也可以利用这个特性，来自己做一些内置事件的监听器来达到和前面一些触发点大致相同的事情
 * // TODO CIdea: 事件如何发布
 * @author: CIdea
 */
@Slf4j
@Component
public class TestApplicationListener implements ApplicationListener {
    /**
     * {@link org.springframework.context.event.ContextRefreshedEvent}
     * ApplicationContext 被初始化或刷新时，该事件被发布。这也可以在ConfigurableApplicationContext接口中使用 refresh()方法来发生。此处的初始化是指：所有的Bean被成功装载，后处理Bean被检测并激活，所有Singleton Bean 被预实例化，ApplicationContext容器已就绪可用。
     * {@link org.springframework.context.event.ContextStartedEvent}
     * 当使用 ConfigurableApplicationContext （ApplicationContext子接口）接口中的 start() 方法启动 ApplicationContext时，该事件被发布。你可以调查你的数据库，或者你可以在接受到这个事件后重启任何停止的应用程序。
     * {@link org.springframework.context.event.ContextStoppedEvent}
     * 当使用 ConfigurableApplicationContext接口中的 stop()停止ApplicationContext 时，发布这个事件。你可以在接受到这个事件后做必要的清理的工作
     * {@link org.springframework.context.event.ContextClosedEvent}
     * 当使用 ConfigurableApplicationContext接口中的 close()方法关闭 ApplicationContext 时，该事件被发布。一个已关闭的上下文到达生命周期末端；它不能被刷新或重启
     * {@link org.springframework.web.context.support.RequestHandledEvent}
     * 这是一个 web-specific 事件，告诉所有 bean HTTP 请求已经被服务。只能应用于使用DispatcherServlet的Web应用。在使用Spring作为前端的MVC控制器时，当Spring处理用户请求结束后，系统会自动触发该事件
     * @param event the event to respond to
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("Custom ApplicationListener, event = {}", event);
    }
}
