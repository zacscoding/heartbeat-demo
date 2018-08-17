package agent;

import agent.context.AppProperties;
import agent.context.module.InitializeGuiceModulesContextListener;
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

    private Server server;

    public static void main(String[] args) throws Exception {
        logger.info(">> Start to agent. args : {}", Arrays.toString(args));
        new Bootstrap().run();
    }

    public void run() throws Exception {
        server = new Server(8089);

        ServletContextHandler rootContext = new ServletContextHandler();
        rootContext.setContextPath("");
        server.setHandler(rootContext);

        rootContext.addEventListener(new InitializeGuiceModulesContextListener());
        rootContext.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        rootContext.addServlet(DefaultServlet.class, "/");

        try {
            server.start();
            server.join();
        } finally {
            if (server != null && server.isRunning()) {
                server.destroy();
            }
        }
    }

}
