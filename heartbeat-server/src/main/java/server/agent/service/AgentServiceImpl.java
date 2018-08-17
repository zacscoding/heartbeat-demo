package server.agent.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import server.agent.Action;
import server.agent.HeartbeatAgent;
import server.agent.Message;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
@Service
public class AgentServiceImpl implements AgentService {

    private static final Logger logger = LoggerFactory.getLogger(AgentServiceImpl.class);

    private final String sendToPrefix = "/result/action/";
    private final long deadAgentCriteria = 10000L;
    private ConcurrentHashMap<HeartbeatAgent, Long> registeredAgent;
    private ConcurrentHashMap<HeartbeatAgent, Queue<Action>> actionQueues;
    private ReadWriteLock lock;
    private ScheduledExecutorService agentHealthChecker;

    @Autowired
    private SimpMessagingTemplate stompMessageTemplate;
    @Autowired
    @Qualifier("agentThreadPool")
    private TaskExecutor taskExecutor;

    @PostConstruct
    private void setUp() {
        this.registeredAgent = new ConcurrentHashMap<>();
        this.actionQueues = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.agentHealthChecker = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread t = new Thread(runnable, "AgentHealthChecker");
            t.setDaemon(true);
            return t;
        });
        agentHealthChecker.scheduleAtFixedRate(this::checkAgentState, 1L, 10L, TimeUnit.SECONDS);
    }

    @Override
    public void receiveBeat(HeartbeatAgent heartbeatAgent, Long timestamp) {
        registeredAgent.put(heartbeatAgent, timestamp);
    }

    @Override
    public List<HeartbeatAgent> getAgents() {
        int size = registeredAgent.size();
        if (size == 0) {
            return Collections.emptyList();
        }

        List<HeartbeatAgent> agents = new ArrayList<>(size);
        Enumeration<HeartbeatAgent> heartbeatAgentEnumeration = registeredAgent.keys();

        while (heartbeatAgentEnumeration.hasMoreElements()) {
            HeartbeatAgent agent = heartbeatAgentEnumeration.nextElement();
            agents.add(agent);
        }

        return agents;
    }

    @Override
    public HeartbeatAgent getAgentByName(String agentName) {
        Enumeration<HeartbeatAgent> heartbeatAgentEnumeration = registeredAgent.keys();

        while (heartbeatAgentEnumeration.hasMoreElements()) {
            HeartbeatAgent agent = heartbeatAgentEnumeration.nextElement();
            if (agent.getAgentName().equals(agentName)) {
                return agent;
            }
        }

        return null;
    }

    @Override
    public void requestAction(HeartbeatAgent agent, Action action) {
        if (agent == null || action == null) {
            logger.warn("Invalid action. agent : {}, action : {}", agent, action);
            return;
        }

        try {
            lock.writeLock().lock();
            Queue<Action> actionQueue = actionQueues.get(agent);

            if (actionQueue == null) {
                actionQueue = new LinkedList<>();
                actionQueues.put(agent, actionQueue);
            }

            actionQueue.offer(action);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Action> pollActions(HeartbeatAgent agent) {
        Queue<Action> actionQueue = actionQueues.get(agent);

        if (actionQueue == null || actionQueue.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            lock.readLock().lock();
            List<Action> actions = new ArrayList<>(actionQueue.size());

            while (!actionQueue.isEmpty()) {
                actions.add(actionQueue.poll());
            }

            return actions;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void handleMessages(HeartbeatAgent agent, List<Message> messages) {
        if (agent == null || messages == null) {
            logger.warn("Invalid message. agent : " + agent + ", messages : " + messages);
            return;
        }

        taskExecutor.execute(() -> {
            for (Message message : messages) {
                if ("action".equals(message.getCommand())) {
                    handleActionMessage(agent, message.getResults());
                } else {
                    throw new UnsupportedOperationException("Not supported message command : " + message.getCommand());
                }
            }
        });
    }

    private void handleActionMessage(HeartbeatAgent agent, Map<String, Object> results) {
        try {
            // TODO :: Change Converter
            Map<String, Object> requestedAction = (Map<String, Object>) results.get("action");
            // Action requestedAction = (Action) results.get("action");
            String destination = appendPaths(sendToPrefix, (String)requestedAction.get("requestId"), agent.getAgentName()
                                , (String)requestedAction.get("serviceName"), (String)requestedAction.get("actionType"));
//            String destination =
//                sendToPrefix + "/" + requestedAction.get("requestId") + "/" + agent.getAgentName() + "/" + requestedAction.get("serviceName") + "/" + requestedAction.get("actionType");
            stompMessageTemplate.convertAndSend(destination, results);
        } catch (Exception e) {
            logger.warn("Invalid message. " + e.getMessage());
        }
    }

    private void checkAgentState() {
        if (registeredAgent.isEmpty()) {
            return;
        }

        long now = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss,SSS");
        for (Map.Entry<HeartbeatAgent, Long> entry : registeredAgent.entrySet()) {
            long lastHeartbeat = entry.getValue();
            if ((now - lastHeartbeat) > deadAgentCriteria) {
                logger.warn("[Dead Agent] : " + entry.getKey() + " - [Last heartbeat] : " + sdf.format(lastHeartbeat));
            }
        }
    }

    private String appendPaths(String ... paths) {
        if (paths == null || paths.length == 0) {
            return "";
        }

        int length = 0;
        for (String path : paths) {
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
            length += path.length();
        }

        StringBuilder sb = new StringBuilder(length);
        sb.append(paths[0]);
        for (int i = 1; i < paths.length; i++) {
            sb.append("/").append(paths[i]);
        }

        return sb.toString();
    }
}