package server.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import server.agent.Action;
import server.agent.ActionType;
import server.agent.HeartbeatAgent;
import server.agent.service.AgentService;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
@Controller
public class ActionController {

    private static final Logger logger = LoggerFactory.getLogger(ActionController.class);

    @Autowired
    private AgentService agentService;

    @GetMapping("/agents")
    @ResponseBody
    public ResponseEntity<List<HeartbeatAgent>> getAgents() {
        return ResponseEntity.ok(agentService.getAgents());
    }

    @MessageMapping("/action/{agentName}/{serviceName}/{actionType}")
    public Map<String, Object> requestAction(@DestinationVariable("agentName") String agentName
                                            , @DestinationVariable("serviceName") String serviceName
                                            , @DestinationVariable("actionType") String actionTypeName) {
        logger.info("request action. agentName : {}, serviceName : {}, actionType : {}", agentName, serviceName, actionTypeName);
        String resultMessage = null;
        boolean success = false;
        Map<String, Object> result = new HashMap<>();
        try {
            HeartbeatAgent agent = agentService.getAgentByName(agentName);
            if (agent == null) {
                resultMessage = "Invalid agent name " + agentName;
                logger.warn("Invalid agent name : " + agentName);
            } else {
                ActionType actionType = ActionType.getType(actionTypeName);
                if (actionType == ActionType.UNKNOWN) {
                    resultMessage = "Invalid action type name  : " + agentName;
                    logger.warn(resultMessage);
                } else {
                    Action action = new Action();
                    action.setActionType(actionType);
                    action.setServiceName(serviceName);
                    action.setRequestId(String.valueOf(System.currentTimeMillis()));

                    agentService.requestAction(agent, action);
                    success = true;
                    resultMessage = "Success to request";
                }
            }
        } catch (Exception e) {
            logger.warn("Exception occur while action request", e);
            resultMessage = e.getMessage();
        }

        result.put("success", success);
        result.put("message", resultMessage);

        return result;
    }
}
