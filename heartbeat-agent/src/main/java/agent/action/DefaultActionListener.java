package agent.action;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultActionListener implements ActionListener {

    private static final Logger logger = LoggerFactory.getLogger("action");

    private BlockingQueue<Action> actionQueue;

    @Inject
    public DefaultActionListener(@Named("actionQueue") BlockingQueue<Action> actionQueue) {
        Objects.requireNonNull(actionQueue, "ActionQueue must be not null");
        this.actionQueue = actionQueue;
    }

    @Override
    public void requestAction(Action action) {
        logger.debug("receive new action : " + action);
        actionQueue.offer(action);
    }
}
