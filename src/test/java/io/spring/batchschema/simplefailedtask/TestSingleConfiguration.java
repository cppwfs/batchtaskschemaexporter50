package io.spring.batchschema.simplefailedtask;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableTask
public class TestSingleConfiguration {

    @Bean
    public ApplicationRunner applicationRunner() {
        return args -> {
            throw new IllegalStateException("Failed Task");
        };
    }
}
