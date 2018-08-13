package agent.sample;

import agent.sample.TempService;
import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */

@Path("/test")
@RequestScoped
public class TempResources {

    private TempService tempService;

    @Inject
    public TempResources(TempService tempService) {
        System.out.println("## TempResource() is called.. : " + tempService);
        this.tempService = tempService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{message}")
    public Response getMessage(@PathParam("message") String message) {
        System.out.println("## Receive message : " + message);
        return Response.status(Response.Status.OK).entity(tempService.generateUUID()).build();
    }
}
