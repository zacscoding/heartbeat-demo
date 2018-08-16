package agent.context.module;

import agent.context.provider.MessageQueueProvider;
import agent.message.MessageQueue;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class MessageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessageQueue.class).toProvider(MessageQueueProvider.class).in(Scopes.SINGLETON);
    }
}
