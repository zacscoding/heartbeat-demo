package agent.heartbeat;

/**
 * @author zacconding
 * @Date 2018-08-16
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
    public String toString() {
        return "HeartbeatAgent{" + "pid=" + pid + ", agentName='" + agentName + '\'' + '}';
    }
}