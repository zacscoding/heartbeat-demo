package agent.sample;

import agent.action.Action;
import agent.action.ActionExecutioner;
import agent.action.ActionListener;
import agent.action.ActionType;
import agent.sample.TempService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;
import java.util.Random;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Singleton
public class TempResources {

    private final TempService tempService;
    private final ActionListener actionListener;
    private final ActionExecutioner actionExecutioner;


    @Inject
    public TempResources(TempService tempService, ActionListener actionListener, ActionExecutioner actionExecutioner) {
        System.out.println("## TempResource() is called.. : " + tempService + ", " + actionListener);
        this.tempService = tempService;
        this.actionListener = actionListener;
        this.actionExecutioner = actionExecutioner;
    }

    //    @GET
    //    @Produces(MediaType.APPLICATION_JSON)
    //    @Path("/{message}")
    //    public Response getMessage(@PathParam("message") String message) {
    //        System.out.println("## Receive message : " + message);
    //        return Response.status(Response.Status.OK).entity(tempService.generateUUID()).build();
    //    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/action")
    public Response requestAction() {
        Action action = new Action();
        ActionType[] actionTypes = new ActionType[] {ActionType.START, ActionType.STOP, ActionType.RESTART};
        Random random = new Random();
        int actionTypeIndex = random.nextInt(actionTypes.length);
        action.setActionType(actionTypes[actionTypeIndex]);
        action.setRequestId(String.valueOf(random.nextInt(1000)));
        action.setServiceName(tempService.generateUUID());

        actionListener.requestAction(action);
        return Response.status(Response.Status.OK).entity(tempService.generateUUID()).build();
    }
}