package io.spring.batchschema.singlejobmultistepfailedsecondstep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;

public class SingleJobMultiStepFailedSecondStepTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and no job parameters established, but the second step fails
     * Then the job is run and the metadata for the job, steps, and tasks are recorded.
     */
    @Test
    void testJobExecution() throws Exception {
        generateImportFile(BatchSingleJobMultiStepFailedSecondStepApplication.class, "singleJobMultiStepFailedSecondStep.load");
    }
}
