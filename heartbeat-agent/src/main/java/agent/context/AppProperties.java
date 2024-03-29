package agent.context;

import agent.util.StringUtil;
import java.io.File;
import java.io.FileReader;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class AppProperties {

    private static final Logger logger = LoggerFactory.getLogger("context");
    private static Object LOCK = new Object();
    private static AppProperties appProperties;

    // heartbeat agent
    private int pid;
    private String agentName;
    private int serverPort;
    private String serverContext;
    private int serverMinThread;
    private int serverMaxThread;
    private long initDelay;
    private long period;

    // admin server
    private List<String> adminServerUrls;

    public static AppProperties getAppProperties() {
        if (appProperties == null) {
            synchronized (LOCK) {
                if (appProperties == null) {
                    appProperties = new AppProperties();
                }
            }
        }

        return appProperties;
    }

    public int getPid() {
        return pid;
    }

    public String getAgentName() {
        return agentName;
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerContext() {
        return serverContext;
    }

    public int getServerMinThread() {
        return serverMinThread;
    }

    public int getServerMaxThread() {
        return serverMaxThread;
    }

    public long getInitDelay() {
        return initDelay;
    }

    public long getPeriod() {
        return period;
    }

    public List<String> getAdminServerUrls() {
        return adminServerUrls;
    }

    private AppProperties() {
        try {
            final String SERVER_PORT = "heartbeat.server.port";
            final String SERVER_CONTEXT = "heartbeat.server.context";
            final String SERVER_THREAD_MIN = "heartbeat.server.minthread";
            final String SERVER_THREAD_MAX = "heartbeat.server.maxthread";
            final String INIT_DELAY = "heartbeat.initdelay";
            final String PERIOD = "heartbeat.period";
            final String ADMIN_SERVER_URLS = "heartbeat.admin.server.urls";
            final String AGENT_NAME = "heartbeat.agent.name";

            Properties properties = new Properties();
            // default load
            properties.load(AppProperties.class.getClassLoader().getResourceAsStream("app.properties"));
            // TODO ::  override & vm args
            String configFilePath = System.getProperty("heartbeat.config.file");
            if (StringUtil.isEmpty(configFilePath)) {
                logger.info("Empty vm args [heartbeat.config.file] >>> Will use default properties");
            } else {
                File configFile = new File(configFilePath);
                if (!configFile.exists()) {
                    logger.warn("Not exist config file : " + configFilePath);
                } else {
                    if (!configFilePath.endsWith(".properties")) {
                        logger.warn("Config file is not property file format. config file : " + configFilePath);
                    } else {
                        properties.load(new FileReader(configFile));
                    }
                }
            }

            // agent
            this.agentName = properties.getProperty(AGENT_NAME);
            this.serverPort = Integer.parseInt(properties.getProperty(SERVER_PORT));
            this.serverContext = properties.getProperty(SERVER_CONTEXT, "");
            this.serverMinThread = Integer.parseInt(properties.getProperty(SERVER_THREAD_MIN));
            this.serverMaxThread = Integer.parseInt(properties.getProperty(SERVER_THREAD_MAX));
            this.pid = extractPID();
            this.initDelay = Long.parseLong(properties.getProperty(INIT_DELAY));
            this.period = Long.parseLong(properties.getProperty(PERIOD));

            // admin server
            this.adminServerUrls = extractAdminServerUrls(properties.getProperty(ADMIN_SERVER_URLS));

            if (logger.isDebugEnabled()) {
                displayProperties();
            }
        } catch (Exception e) {
            logger.warn("failed to load properties", e);
            throw new RuntimeException(e);
        }
    }

    private int extractPID() {
        try {
            String jvmName = ManagementFactory.getRuntimeMXBean().getName();
            return Integer.valueOf(jvmName.split("@")[0]);
        } catch (Exception e) {
            return 0;
        }
    }

    private List<String> extractAdminServerUrls(String urls) {
        if (StringUtil.isEmpty(urls)) {
            return Collections.emptyList();
        }

        StringTokenizer stringTokenizer = new StringTokenizer(urls, ",");

        List<String> ret = new ArrayList<>(stringTokenizer.countTokens());
        while (stringTokenizer.hasMoreTokens()) {
            ret.add(stringTokenizer.nextToken());
        }

        return ret;
    }

    private void displayProperties() {
        StringBuilder sb = new StringBuilder("\n-----------------------------------------------------------------------\n");
        sb.append("Application Properties\n")
          .append("-----------------------------------------------------------------------\n")
          .append("pid : ").append(pid).append("\n")
          .append("agent name : ").append(agentName).append("\n")
          .append("server urls : ").append("\n");

        for(String adminUrl : adminServerUrls) {
            sb.append(adminUrl).append("\n");
        }

        sb.append("server port : ").append(serverPort).append("\n")
          .append("init delay : ").append(initDelay).append(" ms\n")
          .append("period : ").append(period).append(" ms\n")
          .append("-----------------------------------------------------------------------\n");
        logger.debug(sb.toString());
    }
}