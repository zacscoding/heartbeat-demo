package agent.configuration.context.provider;

import agent.action.Action;
import agent.action.ActionListener;
import agent.action.DefaultActionListener;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.concurrent.BlockingQueue;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ActionListenerProvider implements Provider<ActionListener> {

    private BlockingQueue<Action> actionQueue;

    @Inject
    public ActionListenerProvider(@Named("actionQueue") BlockingQueue<Action> actionQueue) {
        this.actionQueue = actionQueue;
    }

    @Override
    public ActionListener get() {
        return new DefaultActionListener(actionQueue);
    }
}