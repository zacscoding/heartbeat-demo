package agent.configuration.context.module;

import agent.action.Action;
import agent.action.ActionExecutioner;
import agent.action.ActionListener;
import agent.configuration.context.provider.ActionExecutionerProvider;
import agent.configuration.context.provider.ActionListenerProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class ActionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ActionListener.class).toProvider(ActionListenerProvider.class).in(Scopes.SINGLETON);
        bind(ActionExecutioner.class).toProvider(ActionExecutionerProvider.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Named("actionQueue")
    @Singleton
    public BlockingQueue<Action> provideActionQueue() {
        return new LinkedBlockingQueue<>();
    }
}