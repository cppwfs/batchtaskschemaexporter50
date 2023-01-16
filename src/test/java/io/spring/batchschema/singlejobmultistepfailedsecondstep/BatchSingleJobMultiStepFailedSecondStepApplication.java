package io.spring.batchschema.singlejobmultistepfailedsecondstep;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(TestSingleJobMultiStepFailedSecondStepConfiguration.class)
public class BatchSingleJobMultiStepFailedSecondStepApplication {

}
