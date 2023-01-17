package io.spring.batchschema.simplefailedtask;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


public class SingleFailedTaskTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single task application that fails
     * Then task is recorded along with failure information.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/batchexportconfig.csv")
    void testTaskExecution(String prefix, String databaseType, long sequenceStartVal) throws Exception {
        generateImportFile(TaskApplication.class, "simplefailedtask.load", prefix, databaseType, sequenceStartVal);
    }

}
