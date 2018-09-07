package agent;

import agent.server.JettyWebServer;
import agent.server.WebServer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class Bootstrap {

    private static final Logger logger = LoggerFactory.getLogger("bootstrap");

    public static void main(String[] args) throws Exception {
        logger.info(">> Start to agent. args : {}", Arrays.toString(args));
        new Bootstrap().start();
    }

    private void start() throws Exception {
        WebServer webserver = new JettyWebServer();
        webserver.start();
        webserver.await();
    }
}
