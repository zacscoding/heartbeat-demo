package agent.message;

import java.util.List;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public interface MessageQueue {

    boolean add(Message message);

    List<Message> pollMessages();
}