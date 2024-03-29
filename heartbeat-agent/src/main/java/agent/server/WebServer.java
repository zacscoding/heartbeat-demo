package agent.server;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public interface WebServer {

    void start() throws Exception;

    void await() throws InterruptedException;
}
