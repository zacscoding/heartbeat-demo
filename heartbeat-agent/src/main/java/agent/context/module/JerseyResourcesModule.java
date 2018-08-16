package agent.context.module;

import agent.receiver.TempResource;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class JerseyResourcesModule extends ServletModule {

    @Override
    protected void configureServlets() {
        bindResources();
        serve("/*").with(GuiceContainer.class);
    }

    private void bindResources() {
        // TODO :: NOT WORKING YET.
        //        PackagesResourceConfig resourceConfig = new PackagesResourceConfig("agent.receiver");
        //        for (Class<?> resource : resourceConfig.getClasses()) {
        //            System.out.println("## Try to bind : " + resource.getName());
        //            bind(resource);
        //        }
        bind(TempResource.class);
    }
}
