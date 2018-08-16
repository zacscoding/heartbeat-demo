package agent.context.provider;

import agent.message.MessageQueue;
import agent.message.MessageQueueImpl;
import com.google.inject.Provider;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class MessageQueueProvider implements Provider<MessageQueue> {

    @Override
    public MessageQueue get() {
        return new MessageQueueImpl();
    }
}
