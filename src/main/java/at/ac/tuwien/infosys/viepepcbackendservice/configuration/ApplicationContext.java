package at.ac.tuwien.infosys.viepepcbackendservice.configuration;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.Executor;

/**
 * Created by philippwaibel on 03/05/16.
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(value="at.ac.tuwien.infosys.viepepcbackendservice")
@EnableWebMvc
@EnableAsync
@PropertySources({
        @PropertySource("classpath:application.properties"),
        @PropertySource("classpath:messagebus-config/messagebus.properties"),
        @PropertySource("classpath:service-registry/service-registry.properties")
})
public class ApplicationContext implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setMaxPoolSize(100);
        executor.setCorePoolSize(80);
        executor.setQueueCapacity(5);
        executor.initialize();
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new CustomAsyncExceptionHandler();
    }

}
