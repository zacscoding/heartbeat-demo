package agent.sample;

import com.google.inject.servlet.GuiceFilter;
import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class SampleBootstrap {

    private Server server;

    public static void main(String[] args) throws Exception {
        new SampleBootstrap().run();
    }

    public void run() throws Exception {
        server = new Server(9090);

        ServletContextHandler rootContext = new ServletContextHandler();
        rootContext.setContextPath("");
        server.setHandler(rootContext);

        rootContext.addEventListener(new SampleConfig());
        rootContext.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        rootContext.addServlet(DefaultServlet.class, "/");

        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
    }
}