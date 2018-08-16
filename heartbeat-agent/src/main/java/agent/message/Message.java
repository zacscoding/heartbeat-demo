package agent.message;

import java.util.Map;

/**
 * @author zacconding
 * @Date 2018-08-13
 * @GitHub : https://github.com/zacscoding
 */
public class Message {

    private final String command;
    private final Map<String, Object> results;

    public Message(String command, Map<String, Object> results) {
        this.command = command;
        this.results = results;
    }

    public String getCommand() {
        return command;
    }

    public Map getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Message{" + "command='" + command + '\'' + ", results=" + results + '}';
    }
}