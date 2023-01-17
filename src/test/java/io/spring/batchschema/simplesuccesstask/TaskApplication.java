package io.spring.batchschema.simplesuccesstask;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestSingleConfiguration.class)
public class TaskApplication {

}
