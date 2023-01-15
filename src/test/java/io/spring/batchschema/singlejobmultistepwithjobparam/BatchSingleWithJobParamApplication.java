package io.spring.batchschema.singlejobmultistepwithjobparam;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestSingleConfiguration.class)
public class BatchSingleWithJobParamApplication {

}
