package agent.action;

/**
 * @author zacconding
 * @Date 2018-08-14
 * @GitHub : https://github.com/zacscoding
 */
public interface ActionExecutioner {

    /**
     * Check whether executing action or not
     */
    boolean isExecuting();

    /**
     * Get remain queue size
     */
    int queueSize();
}