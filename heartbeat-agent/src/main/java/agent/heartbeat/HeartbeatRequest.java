package agent.heartbeat;

import agent.message.Message;
import java.util.List;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatRequest {

    private HeartbeatAgent heartbeatAgent;
    private List<Message> messages;

    public HeartbeatAgent getHeartbeatAgent() {
        return heartbeatAgent;
    }

    public void setHeartbeatAgent(HeartbeatAgent heartbeatAgent) {
        this.heartbeatAgent = heartbeatAgent;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public String toString() {
        return "HeartbeatRequest{" + "heartbeatAgent=" + heartbeatAgent + ", messages=" + messages + '}';
    }
}