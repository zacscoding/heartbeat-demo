package agent.context.module;

import agent.context.provider.HeartbeatClientProvider;
import agent.heartbeat.HeartbeatClient;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HeartbeatClient.class).toProvider(HeartbeatClientProvider.class).in(Scopes.SINGLETON);
    }
}
