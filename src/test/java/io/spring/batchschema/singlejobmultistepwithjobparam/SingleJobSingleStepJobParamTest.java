package io.spring.batchschema.singlejobmultistepwithjobparam;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;

public class SingleJobSingleStepJobParamTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and one job parameters established
     * Then the job is run and the metadata for the job, steps, job param, and tasks are recorded.
     */
    @Test
    void testJobExecution() throws Exception {
        generateImportFile(BatchSingleWithJobParamApplication.class, "singleJobSingleStepJobParam.load", "foo=bar");
    }

}
