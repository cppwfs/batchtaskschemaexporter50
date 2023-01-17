package io.spring.batchschema.simplesuccesstask;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTask
public class TestSingleConfiguration {

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> System.out.println("Hello World");
    }
}
