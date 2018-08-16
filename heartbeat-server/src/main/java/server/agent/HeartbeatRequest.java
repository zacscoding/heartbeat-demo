package server.agent;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatRequest {

    private HeartbeatAgent heartbeatAgent;
    private List<Message> messages;
    private long timestamp;

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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "HeartbeatRequest{" + "heartbeatAgent=" + heartbeatAgent + ", messages=" + messages + '}';
    }
}