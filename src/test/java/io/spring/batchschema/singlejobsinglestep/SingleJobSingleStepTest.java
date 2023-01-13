package io.spring.batchschema.singlejobsinglestep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SingleJobSingleStepTest extends AbstractBatchExport {

    @ParameterizedTest
    @CsvFileSource(resources = "/batchexportconfig.csv")
    void testJobExecution(String prefix, String databaseName) throws Exception {
        generateImportFile(BatchSingleApplication.class, "singleJobSingleStep.load", prefix, databaseName);
    }

}
