package agent.action;

import agent.message.Message;
import agent.message.MessageQueue;
import agent.util.HeartbeatThreadFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
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
    private MessageQueue messageQueue;

    private Thread actionExecutor;
    private Runnable executeJob;
    private boolean isExecuting;

    public DefaultActionExecutioner(BlockingQueue<Action> actionQueue, MessageQueue messageQueue) {
        this.actionQueue = actionQueue;
        this.messageQueue = messageQueue;
        initialize();
    }

    @Override
    public boolean isExecuting() {
        return isExecuting;
    }

    @Override
    public int queueSize() {
        return actionQueue.size();
    }

    @Override
    public void start() {
        if (actionExecutor == null) {
            logger.warn("Failed to start Action Executioner becuz actionExecutor is null");
            return;
        }

        if (actionExecutor.isInterrupted()) {
            logger.warn("Failed to start Action Executioner becuz actionExecutor already is interrupted ");
            return;
        }
        actionExecutor.start();
    }

    @Override
    public void stop() {
        actionExecutor.isInterrupted();
    }

    private boolean execute(Action action) {
        return dummyExecute(action);
    }

    private boolean dummyExecute(Action action) {
        pushMessage(action, ">> Execute action : " + action.toString());
        try {
            int sleepSec = new Random().nextInt(5) + 1;

            pushMessage(action, ">>> temp sleep : " + sleepSec + " sec in execute action");
            for (int i = 0; i < sleepSec; i++) {
                pushMessage(action, ">>>> Execute something... " + i);
                TimeUnit.SECONDS.sleep(1L);
            }

            return true;
        } catch (InterruptedException e) {
            logger.warn("InterruptedException while waiting a few sec in executing action", e);
            pushMessage(action, "InterruptedException while waiting a few sec in executing action" + e.getMessage());
            return false;
        }
    }

    private void pushMessage(Action action, String log) {
        Map<String, Object> results = new HashMap<>();

        results.put("action", action);
        results.put("log", log);

        messageQueue.add(new Message("action", results));
    }

    private void initialize() {
        this.executeJob = () -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Action action = actionQueue.take();
                    isExecuting = true;
                    logger.info("> Take action : " + action);
                    boolean result = execute(action);
                    isExecuting = false;
                    logger.info(">> execute action result : {}\n", result);
                }
            } catch (InterruptedException e) {
                logger.warn("InterruptedException occur while taking action from action queue", e);
            }
        };
        ThreadFactory threadFactory = new HeartbeatThreadFactory("ActionExecutioner", true);
        this.actionExecutor = threadFactory.newThread(executeJob);
    }
}