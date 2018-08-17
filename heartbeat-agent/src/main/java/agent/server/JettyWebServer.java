package agent.server;

import agent.context.AppProperties;
import agent.context.module.InitializeGuiceModulesContextListener;
import com.google.inject.servlet.GuiceFilter;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.NetworkConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
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

    private Server server;
    private int port;
    private String contextPath;
    private int minThread;
    private int maxThread;

    public JettyWebServer() {
        AppProperties appProperties = AppProperties.getAppProperties();
        this.port = appProperties.getServerPort();
        this.contextPath = appProperties.getServerContext();
        this.minThread = appProperties.getServerMinThread();
        this.maxThread = appProperties.getServerMaxThread();
    }

    @Override
    public void start() throws Exception {
        if (server != null && server.isRunning()) {
            logger.warn("Already running jetty server. " + server.toString());
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
            logger.warn("JettyServer is not running.");
            return;
        }

        server.join();
    }

    private ThreadPool createThreadPool() {
        QueuedThreadPool threadPool = new QueuedThreadPool();

        threadPool.setMinThreads(this.minThread);
        threadPool.setMaxThreads(this.maxThread);

        return threadPool;
    }

    private NetworkConnector createConnector() {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        return connector;
    }

    private ServletContextHandler createHandlers() {
        ServletContextHandler rootContext = new ServletContextHandler();

        rootContext.setContextPath(contextPath);
        rootContext.addEventListener(new InitializeGuiceModulesContextListener());
        rootContext.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        rootContext.addServlet(DefaultServlet.class, "/");

        return rootContext;
    }
}
