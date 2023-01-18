package io.spring.batchschema.singlejobexitcode;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;

public class SingleJobMultiStepExitCodeTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and no job parameters established, but the second step fails
     * Then the job is run and the metadata for the job, steps, and tasks are recorded and exit code of 1 is recorded for task.
     */
    @Test
    void testJobExecution() throws Exception {
        generateImportFile(BatchSingleJobMultiStepExitCodeApplication.class, "singleJobMultiStepFailedSecondStepExitCode.load",
                "--spring.cloud.task.batch.fail-on-job-failure=true");
    }
}
