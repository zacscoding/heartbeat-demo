//package agent.message;
//
//import agent.util.DaemonThreadFactory;
//import com.google.inject.Inject;
//import java.util.List;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
///**
// * @author zacconding
// * @Date 2018-08-16
// * @GitHub : https://github.com/zacscoding
// */
//public class ConsoleOutputMessage {
//
//    private static final Logger logger = LoggerFactory.getLogger("temp");
//
//
//    private final MessageQueue messageQueue;
//    private ScheduledExecutorService scheduledExecutor;
//
//    // @Inject
//    public ConsoleOutputMessage(MessageQueue messageQueue) {
//        logger.info("ConsoleOutputMessage is created. messageQueue : {}", messageQueue);
//        this.messageQueue = messageQueue;
//        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor(DaemonThreadFactory.getInstance("Temp Thread"));
//        scheduledExecutor.scheduleAtFixedRate(() -> display(), 1000L, 1000L, TimeUnit.MILLISECONDS);
//    }
//
//    private void display() {
//        System.out.println("display is called..");
//        List<Message> messages = messageQueue.pollMessages();
//        logger.info("-----------------------------------------------------------------------------------------------");
//        logger.info("> Get messages. size : {}", messages.size());
//        for (int i = 0; i < messages.size(); i++) {
//            Message message = messages.get(i);
//            logger.info(">>> [Message #{}]. command : {} | results : {}", message.getCommand(), message.getResults());
//        }
//        logger.info("-----------------------------------------------------------------------------------------------");
//    }
//
//    private Thread createDaemonThread(Runnable r) {
//        Thread t = new Thread();
//
//        t.setName("ConsoleOutput Thread");
//        t.setDaemon(true);
//
//        return t;
//    }
//}
