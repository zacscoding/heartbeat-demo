package agent.receiver;

import agent.action.Action;
import agent.action.ActionExecutioner;
import agent.action.ActionListener;
import agent.action.ActionType;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
@Path("/temp")
@Singleton
public class TempResource {

    private static final Logger logger = LoggerFactory.getLogger("receiver");

    private final ActionListener actionListener;
    private final ActionExecutioner actionExecutioner;

    @Inject
    public TempResource(ActionListener actionListener, ActionExecutioner actionExecutioner) {
        Objects.requireNonNull(actionListener, "actionListener must be not null");
        Objects.requireNonNull(actionExecutioner, "actionExecutioner must be not null");

        logger.trace("TempResource is created. actionListener : {} | actionExecutioner : {}", actionListener, actionExecutioner);

        this.actionListener = actionListener;
        this.actionExecutioner = actionExecutioner;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/action")
    public Response requestAction() {
        String uuid = generateUUID();
        Action action = new Action();
        ActionType[] actionTypes = new ActionType[] {ActionType.START, ActionType.STOP, ActionType.RESTART};
        Random random = new Random();
        int actionTypeIndex = random.nextInt(actionTypes.length);
        action.setActionType(actionTypes[actionTypeIndex]);
        action.setRequestId(String.valueOf(random.nextInt(1000)));
        action.setServiceName(uuid);

        actionListener.requestAction(action);
        return Response.status(Response.Status.OK).entity(uuid).build();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
}
