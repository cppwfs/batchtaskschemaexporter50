package io.spring.batchschema.singlejobsinglestep;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestSingleConfiguration.class)
public class BatchSingleApplication {

}
