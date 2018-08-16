package server.agent;

import java.util.Objects;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatAgent {

    private int pid;
    private String agentName;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }


    @Override
    public int hashCode() {
        return this.agentName.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HeartbeatAgent)) {
            return false;
        }
        HeartbeatAgent that = (HeartbeatAgent) o;
        return Objects.equals(getAgentName(), that.getAgentName());
    }

    @Override
    public String toString() {
        return "HeartbeatAgent{" + "pid=" + pid + ", agentName='" + agentName + '\'' + '}';
    }
}