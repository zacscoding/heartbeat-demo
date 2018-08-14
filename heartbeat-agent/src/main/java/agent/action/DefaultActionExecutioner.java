package agent.action;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class DefaultActionExecutioner implements ActionExecutioner {

    private static final Logger logger = LoggerFactory.getLogger("action");

    private BlockingQueue<Action> actionQueue;
    private Thread actionExecutor;
    private boolean isExecuting;

    @Inject
    public DefaultActionExecutioner(@Named("actionQueue") BlockingQueue<Action> actionQueue) {
        Objects.requireNonNull(actionQueue, "ActionQueue must be not null");
        this.actionQueue = actionQueue;

        actionExecutor = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Action action = actionQueue.take();
                    isExecuting = true;
                    logger.info("> Take action : " + action);
                    boolean result = execute(action);
                    isExecuting = false;
                    logger.info(">>>>>>>> execute action result : {}\n", result);
                }
            } catch (InterruptedException e) {
                logger.warn("InterruptedException occur while taking action from action queue", e);
            }
        });

        actionExecutor.setDaemon(true);
        actionExecutor.start();
    }

    @Override
    public boolean isExecuting() {
        return isExecuting;
    }

    @Override
    public int queueSize() {
        return actionQueue.size();
    }

    private boolean execute(Action action) {
        logger.info(">> Execute action : {}", action.toString());
        // TODO :: put messages
        try {
            int sleepSec = new Random().nextInt(5) + 1;

            logger.info(">>> temp sleep : {} sec in execute action", sleepSec);

            for (int i = 0; i < sleepSec; i++) {
                logger.info(">>>> Execute something... " + i);
                TimeUnit.SECONDS.sleep(1L);
            }

            return true;
        } catch (InterruptedException e) {
            logger.warn("InterruptedException while waiting a few sec in executing action", e);
            return false;
        }
    }
}