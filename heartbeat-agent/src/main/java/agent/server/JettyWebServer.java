package agent.server;

import agent.context.AppProperties;
import agent.context.module.InitializeGuiceModulesContextListener;
import agent.util.StringUtil;
import com.google.inject.servlet.GuiceFilter;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.util.thread.ThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class JettyWebServer implements WebServer {

    private static final Logger logger = LoggerFactory.getLogger("bootstrap");
    private static final int SERVER_MIN_THREAD = 10;
    private static final int SERVER_MAX_THREAD = 100;

    private Server server;
    private int port;
    private String host;
    private String contextPath;

    private int minThreads;
    private int maxThreads;

    public JettyWebServer() {
        AppProperties appProperties = AppProperties.getAppProperties();

        this.host = appProperties.getServerHost();
        this.port = appProperties.getServerPort();
        this.contextPath = appProperties.getContextPath();

        this.minThreads = SERVER_MIN_THREAD;
        this.maxThreads = SERVER_MAX_THREAD;
    }

    @Override
    public void start() throws Exception {
        if (server != null && server.isRunning()) {
            logger.warn(">> JettyServer is already running");
            return;
        }

        server = new Server(createThreadPool());
        server.addConnector(createConnector());
        server.setHandler(createHandlers());
        server.setStopAtShutdown(true);

        server.start();
    }

    @Override
    public void await() throws InterruptedException {
        if (server == null || !server.isRunning()) {
            System.out.println("JettyWebServer is not running.");
            return;
        }

        server.join();
    }

    private ThreadPool createThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool();

        threadPool.setMinThreads(minThreads);
        threadPool.setMaxThreads(maxThreads);

        return threadPool;
    }

    private NetworkConnector createConnector() {
        ServerConnector connector = new ServerConnector(server);

        connector.setPort(port);
        if (StringUtil.isNotEmpty(host)) {
            connector.setHost(host);
        }

        return connector;
    }

    private HandlerCollection createHandlers() {
        ServletContextHandler rootContext = new ServletContextHandler();
        rootContext.setContextPath(contextPath);
        server.setHandler(rootContext);

        rootContext.addEventListener(new InitializeGuiceModulesContextListener());
        rootContext.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        rootContext.addServlet(DefaultServlet.class, "/");

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[] {rootContext});

        return handlerCollection;
    }
}
