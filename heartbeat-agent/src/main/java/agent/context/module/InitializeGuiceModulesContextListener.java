package agent.context.module;

import agent.action.ActionExecutioner;
import agent.heartbeat.HeartbeatClient;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import javax.servlet.ServletContextEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class InitializeGuiceModulesContextListener extends GuiceServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger("context");

    private Injector injector;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        loadContext();
    }

    @Override
    protected Injector getInjector() {
        injector = Guice.createInjector(new ActionModule(), new MessageModule(), new HeartbeatModule(), new JerseyResourcesModule());
        return injector;
    }

    private void loadContext() {
        ActionExecutioner actionExecutioner = injector.getInstance(ActionExecutioner.class);
        logger.trace("ActionExecutioner : " + actionExecutioner);
        actionExecutioner.start();

        HeartbeatClient heartbeatClient = injector.getInstance(HeartbeatClient.class);
        logger.trace("HeartbeatClient : " + actionExecutioner);
        heartbeatClient.start();
    }
}