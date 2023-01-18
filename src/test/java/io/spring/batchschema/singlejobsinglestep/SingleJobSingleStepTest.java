package io.spring.batchschema.singlejobsinglestep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;


public class SingleJobSingleStepTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has a single step and no job parameters established
     * Then the job is run and the metadata for the job, steps, and tasks are recorded.
     */
    @Test
    void testJobExecution() throws Exception {
        generateImportFile(BatchSingleApplication.class, "singleJobSingleStep.load");
    }

}
