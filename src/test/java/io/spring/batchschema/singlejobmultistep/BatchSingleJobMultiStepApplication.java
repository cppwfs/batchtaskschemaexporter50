package io.spring.batchschema.singlejobmultistep;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestSingleJobMultiStepConfiguration.class)
public class BatchSingleJobMultiStepApplication {

}
