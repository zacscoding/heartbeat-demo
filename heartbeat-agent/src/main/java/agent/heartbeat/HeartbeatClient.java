package agent.heartbeat;

import agent.action.ActionListener;
import agent.message.MessageQueue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public interface HeartbeatClient {

    void start();

    void stop();
}