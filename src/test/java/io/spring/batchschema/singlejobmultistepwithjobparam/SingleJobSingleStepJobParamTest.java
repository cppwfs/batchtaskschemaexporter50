package io.spring.batchschema.singlejobmultistepwithjobparam;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SingleJobSingleStepJobParamTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and one job parameters established
     * Then the job is run and the metadata for the job, steps, job param, and tasks are recorded.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/batchexportconfig.csv")
    void testJobExecution(String prefix, String databaseType, long sequenceStartVal) throws Exception {
        generateImportFile(BatchSingleWithJobParamApplication.class, "singleJobSingleStepJobParam.load", prefix, databaseType, "foo=bar", sequenceStartVal);
    }

}
