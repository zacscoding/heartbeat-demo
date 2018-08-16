package agent.exception;


import javax.ws.rs.core.Response;

/**
 * @author zacconding
 * @Date 2018-08-16
 * @GitHub : https://github.com/zacscoding
 */
public class HeartbeatException extends Exception {

    private Response response;

    public HeartbeatException(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }
}
