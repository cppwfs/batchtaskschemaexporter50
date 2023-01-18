package io.spring.batchschema.simplefailedtask;

import io.spring.batchschema.AbstractBatchExport;
import org.junit.jupiter.api.Test;


public class SingleFailedTaskTest extends AbstractBatchExport {

    /**
     * Given an empty database
     * When a user needs to execute a single task application that fails
     * Then task is recorded along with failure information.
     */
    @Test
    void testTaskExecution() throws Exception {
        generateImportFile(TaskApplication.class, "simplefailedtask.load");
    }

}
