package agent.context.provider;

import agent.action.ActionListener;
import agent.heartbeat.DefaultHeartbeatClient;
import agent.heartbeat.HeartbeatClient;
import agent.message.MessageQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatClientProvider implements Provider<HeartbeatClient> {

    private MessageQueue messageQueue;
    private ActionListener actionListener;

    @Inject
    public HeartbeatClientProvider(MessageQueue messageQueue, ActionListener actionListener) {
        this.messageQueue = messageQueue;
        this.actionListener = actionListener;
    }


    @Override
    public HeartbeatClient get() {
        return new DefaultHeartbeatClient(messageQueue, actionListener);
    }
}