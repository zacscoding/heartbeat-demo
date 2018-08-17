package server.agent.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import server.agent.Action;
import server.agent.HeartbeatAgent;
import server.agent.HeartbeatRequest;
import server.agent.HeartbeatResponse;
import server.agent.service.AgentService;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
@RestController
public class HeartbeatController {

    private static final Logger logger = LoggerFactory.getLogger(HeartbeatController.class);

    @Autowired
    private AgentService agentService;

    @PostMapping(value = "/heartbeat")
    public ResponseEntity<HeartbeatResponse> heartbeat(@RequestBody HeartbeatRequest request) {
        HeartbeatAgent agent = null;
        if (request == null || (agent = request.getHeartbeatAgent()) == null) {
            logger.warn("Bad heartbeat request. request : {} | agent : {}", request, (request == null ? "null" : request.getHeartbeatAgent()));
            return ResponseEntity.badRequest().build();
        }

        logger.trace("## Heartbeat request : " + request);

        // regist agent
        agentService.receiveBeat(agent, request.getTimestamp());

        // async handle
        agentService.handleMessages(agent, request.getMessages());

        // include actions
        HeartbeatResponse response = new HeartbeatResponse();
        response.setActions(agentService.pollActions(agent));

        logger.trace("## Heartbeat response : " + response);

        return ResponseEntity.ok(response);
    }
}
