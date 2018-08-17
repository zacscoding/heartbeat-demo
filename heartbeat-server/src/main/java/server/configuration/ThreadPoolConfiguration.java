package server.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author zacconding
 * @Date 2018-08-17
 * @GitHub : https://github.com/zacscoding
 */
@Configuration
public class ThreadPoolConfiguration {

    @Bean
    public TaskExecutor agentThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(500);
        executor.setThreadNamePrefix("agent_task_executor_thread");
        executor.initialize();
        executor.setDaemon(true);
        return executor;
    }
}
