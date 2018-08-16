package server.agent;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatResponse {

    private List<Action> actions;

    public boolean hasActions() {
        return actions != null && actions.size() > 0;
    }

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "HeartbeatResponse{" + "actions=" + actions + '}';
    }
}