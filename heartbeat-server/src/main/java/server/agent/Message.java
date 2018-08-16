package server.agent;

import java.util.Map;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
public class Message {

    private String command;
    private Map<String, Object> results;

    public void setCommand(String command) {
        this.command = command;
    }

    public void setResults(Map<String, Object> results) {
        this.results = results;
    }

    public String getCommand() {
        return command;
    }

    public Map<String, Object> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "Message{" + "command='" + command + '\'' + ", results=" + results + '}';
    }
}