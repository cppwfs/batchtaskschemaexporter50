package io.spring.batchschema.simplesuccesstask;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;


public class SingleTaskTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single task application
     * Then task is recorded.
     */
    @Test
    void testTaskExecution() throws Exception {
        generateImportFile(TaskApplication.class, "simplesuccesstask.load");
    }

}
