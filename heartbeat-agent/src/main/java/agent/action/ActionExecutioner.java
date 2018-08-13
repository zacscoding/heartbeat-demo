package agent.action;

import com.google.inject.Inject;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class ActionExecutioner extends Thread {

    private static final Logger logger = LoggerFactory.getLogger("action");

    private final BlockingQueue<Action> actionQueue;

    @Inject
    public ActionExecutioner(BlockingQueue<Action> actionQueue) {
        this.actionQueue = actionQueue;
        this.setDaemon(true);
        this.start();
    }

    private void executeAction(Action action) {
        logger.info("## Try to execute action.");

    }

    public void run() {
        try {
            while (!isInterrupted()) {
                Action action = actionQueue.take();
                executeAction(action);
            }
        } catch (InterruptedException e) {
            logger.warn("InterrupedException while taking actions from queue.", e);
        }
    }
}
