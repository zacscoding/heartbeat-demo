package agent.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zacconding
 * @Date 2018-08-15
 * @GitHub : https://github.com/zacscoding
 */
public class MessageQueueImpl implements MessageQueue {

    private static final Logger logger = LoggerFactory.getLogger("message");

    private ReadWriteLock lock;
    private Queue<Message> messageQueue;

    public MessageQueueImpl() {
        this.lock = new ReentrantReadWriteLock();
        messageQueue = new LinkedList<>();
    }

    @Override
    public boolean add(Message message) {
        logger.trace("add message : {}", message);
        if (message == null) {
            return false;
        }

        try {
            lock.writeLock().lock();
            messageQueue.offer(message);
            return true;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public List<Message> pollMessages() {
        logger.trace("pollMessages. size : {}", messageQueue.size());
        if (messageQueue.size() == 0) {
            return Collections.emptyList();
        }

        try {
            lock.readLock().lock();
            List<Message> ret = new ArrayList<>(messageQueue.size());

            while (!messageQueue.isEmpty()) {
                ret.add(messageQueue.poll());
            }

            return ret;
        } finally {
            lock.readLock().unlock();
        }
    }
}
