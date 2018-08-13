package agent.sample;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class TempServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TempService.class).to(TempServiceImpl.class).in(Singleton.class);
    }
}
