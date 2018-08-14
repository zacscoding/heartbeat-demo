package agent.sample;

import agent.configuration.context.module.ActionModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class SampleConfig extends GuiceServletContextListener {

    protected Injector getInjector() {
        System.out.println("## getInjector is called..");
        return Guice.createInjector(new ServletModule() {
            @Override
            protected void configureServlets() {
                System.out.println("getInjector() is called..");
                /* bind the REST resources */
//                PackagesResourceConfig resourceConfig = new PackagesResourceConfig("agent.resource");
//                for (Class<?> resource : resourceConfig.getClasses()) {
//                    System.out.println("## Try to bind : " + resource.getName());
//                    bind(resource);
//                }

                bind(TempResources.class).in(Scopes.SINGLETON);

                /* bind jackson converters for JAXB/JSON serialization */
                //                bind(MessageBodyReader.class).to(JacksonJsonProvider.class);
                //                bind(MessageBodyWriter.class).to(JacksonJsonProvider.class);

                serve("/*").with(GuiceContainer.class);
            }
        }, new TempServiceModule(), new ActionModule());
    }
}
