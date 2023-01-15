package io.spring.batchschema.singlejobmultistep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SingleJobMultiStepTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single batch job that has multiple steps and no job parameters established
     * Then the job is run and the metadata for the job, steps, and tasks are recorded.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/batchexportconfig.csv")
    void testJobExecution(String prefix, String databaseType, long sequenceStartVal) throws Exception {
        generateImportFile(BatchSingleJobMultiStepApplication.class, "singleJobMultiStep.load", prefix, databaseType, sequenceStartVal);
    }
}
