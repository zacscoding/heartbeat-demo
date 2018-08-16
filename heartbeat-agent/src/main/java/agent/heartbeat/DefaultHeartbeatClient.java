package agent.heartbeat;

import agent.action.Action;
import agent.action.ActionListener;
import agent.context.AppProperties;
import agent.exception.HeartbeatException;
import agent.message.MessageQueue;
import agent.util.HeartbeatThreadFactory;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultHeartbeatClient implements HeartbeatClient {

    private static final Logger logger = LoggerFactory.getLogger("heartbeat");

    private final MessageQueue messageQueue;
    private final ActionListener actionListener;
    private ScheduledExecutorService scheduledExecutor;
    private List<String> adminServerUrls;
    private HeartbeatAgent heartbeatAgent;
    private Client restClient;

    public DefaultHeartbeatClient(MessageQueue messageQueue, ActionListener actionListener) {
        this.messageQueue = messageQueue;
        this.actionListener = actionListener;
        initialize();
    }

    @Override
    public void start() {
        logger.info(">> Heartbeat started");
        AppProperties appProperties = AppProperties.getAppProperties();
        scheduledExecutor.scheduleAtFixedRate(this::doBeats, appProperties.getInitDelay(), appProperties.getPeriod(), TimeUnit.MILLISECONDS);
    }

    @Override
    public void stop() {
        scheduledExecutor.shutdown();
        try {
            scheduledExecutor.awaitTermination(3000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        logger.info(">> Heartbeat stopped");
    }

    private void doBeats() {
        if (adminServerUrls.isEmpty()) {
            logger.debug(">> Admin Server endpoint is empty >> skip doBeat()");
            return;
        }

        HeartbeatRequest request = new HeartbeatRequest();
        request.setHeartbeatAgent(heartbeatAgent);
        request.setMessages(messageQueue.pollMessages());
        request.setTimestamp(System.currentTimeMillis());

        for (String serverUrl : adminServerUrls) {
            StringBuilder requestUrl = new StringBuilder(serverUrl.length() + 10);
            requestUrl.append(serverUrl).append("/heartbeat");
            try {
                tryBeat(requestUrl.toString(), request);
            } catch (HeartbeatException e) {
                logger.warn("Invalid response in tryBeat(). response : " + e.getResponse());
            } catch (Exception e) {
                if (e.getClass().isAssignableFrom(ProcessingException.class)) {
                    logger.warn("ProcessingException occur while tryBeat(). server url : " + serverUrl + ", reason : " + e.getMessage());
                } else {
                    logger.warn("Exception occur while tryBeat(). server url : " + serverUrl, e);
                }
            }
        }
    }

    private void tryBeat(String serverUrl, HeartbeatRequest heartbeatRequest) throws HeartbeatException {
        WebTarget target = restClient.target(serverUrl);
        Invocation.Builder invokeBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invokeBuilder.post(Entity.entity(heartbeatRequest, MediaType.APPLICATION_JSON));
        logger.debug(">> Heartbeat reseult. url : {} , status : {}", serverUrl, response.getStatus());

        if ((response.getStatus() != Response.Status.OK.getStatusCode() || !response.hasEntity())) {
            throw new HeartbeatException(response);
        }

        HeartbeatResponse heartbeatResponse = response.readEntity(HeartbeatResponse.class);
        if (heartbeatResponse.hasActions()) {
            for (Action action : heartbeatResponse.getActions()) {
                actionListener.requestAction(action);
            }
        }
    }

    private void initialize() {
        AppProperties appProperties = AppProperties.getAppProperties();

        // this agent
        this.heartbeatAgent = new HeartbeatAgent();
        this.heartbeatAgent.setAgentName(appProperties.getAgentName());
        this.heartbeatAgent.setPid(appProperties.getPid());

        // heartbeat scheduler
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor(new HeartbeatThreadFactory("HeartbeatThread", true));

        // server endpoints
        this.adminServerUrls = appProperties.getAdminServerUrls();
        this.restClient = ClientBuilder.newClient();
    }
}