package server.agent.service;

import java.util.List;
import server.agent.Action;
import server.agent.HeartbeatAgent;
import server.agent.Message;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public interface AgentService {

    /**
     * update agent heartbeat timestamp
     */
    void receiveBeat(HeartbeatAgent heartbeatAgent, Long timestamp);

    /**
     * Get all agents
     */
    List<HeartbeatAgent> getAgents();

    /**
     * Get agent by agent name
     */
    HeartbeatAgent getAgentByName(String agentName);

    /**
     * Request action
     * > Will included in heartbeat response
     */
    void requestAction(HeartbeatAgent agent, Action action);

    /**
     * poll all actions
     */
    List<Action> pollActions(HeartbeatAgent agent);

    void handleMessages(HeartbeatAgent agent, List<Message> messages);
}