package io.spring.batchschema.singlejobmultistep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;

public class SingleJobMultiStepTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and no job parameters established
     * Then the job is run and the metadata for the job, steps, and tasks are recorded.
     */
    @Test
    void testJobExecution() throws Exception {
        generateImportFile(BatchSingleJobMultiStepApplication.class, "singleJobMultiStep.load");
    }
}
