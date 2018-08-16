package agent.context.provider;

import agent.action.Action;
import agent.action.ActionExecutioner;
import agent.action.DefaultActionExecutioner;
import agent.message.MessageQueue;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;
import java.util.concurrent.BlockingQueue;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public class ActionExecutionerProvider implements Provider<ActionExecutioner> {

    private BlockingQueue<Action> actionQueue;
    private MessageQueue messageQueue;

    @Inject
    public ActionExecutionerProvider(@Named("actionQueue") BlockingQueue<Action> actionQueue, MessageQueue messageQueue) {
        this.actionQueue = actionQueue;
        this.messageQueue = messageQueue;
    }

    @Override
    public ActionExecutioner get() {
        return new DefaultActionExecutioner(actionQueue, messageQueue);
    }
}