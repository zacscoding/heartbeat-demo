package agent;

import agent.context.AppProperties;
import agent.context.module.InitializeGuiceModulesContextListener;
import agent.server.JettyWebServer;
import agent.server.WebServer;
import com.google.inject.servlet.GuiceFilter;
import java.util.Arrays;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger("bootstrap");
    private WebServer webServer;

    public static void main(String[] args) throws Exception {
        new Bootstrap().run();
    }

    private void run() throws Exception {
        webServer = new JettyWebServer();
        webServer.start();
        webServer.await();
    }
}
