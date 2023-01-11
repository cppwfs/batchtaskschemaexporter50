package io.spring.batchschema.singlejobmultistep;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

public class SingleJobMultiStepTest extends AbstractBatchExport {

    @ParameterizedTest
    @CsvFileSource(resources = "/prefix.csv")
    void testJobExecution(String prefix) throws Exception {
        System.out.println("....." + prefix);
        generateImportFile(BatchSingleJobMultiStepApplication.class, "singleJobMultiStep.load", prefix);
    }
}
