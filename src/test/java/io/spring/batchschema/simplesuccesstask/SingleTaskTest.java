package io.spring.batchschema.simplesuccesstask;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;


public class SingleTaskTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single task application
     * Then task is recorded.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/batchexportconfig.csv")
    void testTaskExecution(String prefix, String databaseType, long sequenceStartVal) throws Exception {
        generateImportFile(TaskApplication.class, "simplesuccesstask.load", prefix, databaseType, sequenceStartVal);
    }

}
