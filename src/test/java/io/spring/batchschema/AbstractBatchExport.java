package io.spring.batchschema;


import org.junit.jupiter.api.BeforeEach;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.core.io.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.*;
import java.util.List;
import java.util.Map;


@Testcontainers
public abstract class AbstractBatchExport {

    protected MariaDbDataSource dataSource;

    @Container
    public static final MariaDBContainer mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @BeforeEach
    void setUp() throws Exception {
        dataSource = new MariaDbDataSource();
        dataSource.setUser(mariaDB.getUsername());
        dataSource.setPassword(mariaDB.getPassword());
        dataSource.setUrl(mariaDB.getJdbcUrl());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-mariadb.sql"));
        databasePopulator.addScript(new ClassPathResource("/org/springframework/cloud/task/schema-mariadb.sql"));
        databasePopulator.execute(dataSource);
    }


    public void configureImportFile(String nameOfFile, String prefix, String databaseType, long startValue) throws Exception {
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource outResource = resourceLoader.getResource("./batchloadfiles/" + nameOfFile);
        WritableResource writableResource = (WritableResource) outResource;
        OutputStream out = writableResource.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            generateInserts(writer, prefix);
            setSequences(writer, startValue, prefix, databaseType);
        } finally {
            writer.close();
        }
    }

    public void generateInserts(BufferedWriter writer, String prefix) throws Exception {
        String batchPrefix = (prefix.equals("default") ? "BATCH" : prefix);
        String taskPrefix = (prefix.equals("default") ? "TASK" : prefix);

        JdbcTemplate template = new JdbcTemplate(dataSource);
        generateBatchInstanceInserts(writer, template, batchPrefix);
        generateBatchJobExecutionInserts(writer, template, batchPrefix);
        generateBatchStepExecutionInserts(writer, template, batchPrefix);
        generateBatchExecutionContextInserts(writer, template, batchPrefix);
        generateBatchExecutionWithParamInserts(writer, template, batchPrefix);
        generateBatchStepExecutionContextInserts(writer, template, batchPrefix);
        generateTaskExecutionInserts(writer, template, taskPrefix);
        generateTaskExecutionParamInserts(writer, template, taskPrefix);
        generateTaskBatchInserts(writer, template, taskPrefix);
    }

    private void generateBatchJobExecutionInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select CREATE_TIME, END_TIME, EXIT_CODE, EXIT_MESSAGE, " +
                "JOB_EXECUTION_ID, JOB_INSTANCE_ID, LAST_UPDATED, START_TIME, " +
                "STATUS, VERSION FROM " + prefix + "_JOB_EXECUTION");
        for (Map<String, Object> row : result) {
            String batchJobExecution = "insert into " + prefix + "_JOB_EXECUTION (CREATE_TIME, END_TIME, EXIT_CODE, EXIT_MESSAGE, " +
                    "JOB_EXECUTION_ID, JOB_INSTANCE_ID, LAST_UPDATED, START_TIME, " +
                    "STATUS, VERSION) " +
                    "values ('" + row.get("CREATE_TIME") + "'," +
                    "'" + row.get("END_TIME") + "'," +
                    "'" + row.get("EXIT_CODE") + "'," +
                    "'" + row.get("EXIT_MESSAGE") + "'," +
                    "'" + row.get("JOB_EXECUTION_ID") + "'," +
                    "'" + row.get("JOB_INSTANCE_ID") + "'," +
                    "'" + row.get("LAST_UPDATED") + "'," +
                    "'" + row.get("START_TIME") + "'," +
                    "'" + row.get("STATUS") + "'," +
                    "'" + row.get("VERSION") + "'" +
                    ");\n";
            System.out.println(batchJobExecution);
            writer.write(batchJobExecution);
        }
    }

    private void generateBatchInstanceInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("SELECT JOB_INSTANCE_ID, JOB_KEY, JOB_NAME, VERSION " +
                "FROM " + prefix + "_JOB_INSTANCE");
        for (Map<String, Object> row : result) {
            String batchInstance = "insert into " + prefix + "_JOB_INSTANCE (JOB_INSTANCE_ID, JOB_KEY, JOB_NAME, VERSION) " +
                    "values ('" + row.get("JOB_INSTANCE_ID") + "'," +
                    "'" + row.get("JOB_KEY") + "'," +
                    "'" + row.get("JOB_NAME") + "'," +
                    "'" + row.get("VERSION") + "'" +
                    ");\n";
            System.out.println(batchInstance);
            writer.write(batchInstance);
        }
    }

    private void generateBatchStepExecutionInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select COMMIT_COUNT, CREATE_TIME, END_TIME, EXIT_CODE, " +
                "EXIT_MESSAGE, FILTER_COUNT, JOB_EXECUTION_ID, LAST_UPDATED, PROCESS_SKIP_COUNT, READ_COUNT, " +
                "READ_SKIP_COUNT, ROLLBACK_COUNT, START_TIME, STATUS, STEP_EXECUTION_ID, STEP_NAME, " +
                "VERSION, WRITE_COUNT, WRITE_SKIP_COUNT FROM " + prefix + "_STEP_EXECUTION");
        for (Map<String, Object> row : result) {
            String batchStepExecution = "insert into " + prefix + "_STEP_EXECUTION (COMMIT_COUNT, CREATE_TIME, END_TIME, EXIT_CODE, " +
                    "EXIT_MESSAGE, FILTER_COUNT, JOB_EXECUTION_ID, LAST_UPDATED, PROCESS_SKIP_COUNT, READ_COUNT, " +
                    "READ_SKIP_COUNT, ROLLBACK_COUNT, START_TIME, STATUS, STEP_EXECUTION_ID, STEP_NAME, " +
                    "VERSION, WRITE_COUNT, WRITE_SKIP_COUNT) " +
                    "values ('" + row.get("COMMIT_COUNT") + "'," +
                    "'" + row.get("CREATE_TIME") + "'," +
                    "'" + row.get("END_TIME") + "'," +
                    "'" + row.get("EXIT_CODE") + "'," +
                    "'" + row.get("EXIT_MESSAGE") + "'," +
                    "'" + row.get("FILTER_COUNT") + "'" + "," +
                    "'" + row.get("JOB_EXECUTION_ID") + "'," +
                    "'" + row.get("LAST_UPDATED") + "'," +
                    "'" + row.get("PROCESS_SKIP_COUNT") + "'," +
                    "'" + row.get("READ_COUNT") + "'," +
                    "'" + row.get("READ_SKIP_COUNT") + "'," +
                    "'" + row.get("ROLLBACK_COUNT") + "'," +
                    "'" + row.get("START_TIME") + "'," +
                    "'" + row.get("STATUS") + "'," +
                    "'" + row.get("STEP_EXECUTION_ID") + "'," +
                    "'" + row.get("STEP_NAME") + "'," +
                    "'" + row.get("VERSION") + "'," +
                    "'" + row.get("WRITE_COUNT") + "'," +
                    "'" + row.get("WRITE_SKIP_COUNT") + "'" +
                    ");\n";
            System.out.println(batchStepExecution);
            writer.write(batchStepExecution);
        }
    }

    private void generateBatchExecutionContextInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("SELECT JOB_EXECUTION_ID, SERIALIZED_CONTEXT, SHORT_CONTEXT " +
                "FROM " + prefix + "_JOB_EXECUTION_CONTEXT");
        for (Map<String, Object> row : result) {
            String batchExecutionContextInstance = "insert into " + prefix + "_JOB_EXECUTION_CONTEXT (JOB_EXECUTION_ID, SERIALIZED_CONTEXT, SHORT_CONTEXT)" +
                    "values ('" + row.get("JOB_EXECUTION_ID") + "'," +
                    replaceNullWithNull(row.get("SERIALIZED_CONTEXT")) + "," +
                    "'" + row.get("SHORT_CONTEXT") + "'" +
                    ");\n";
            System.out.println(batchExecutionContextInstance);
            writer.write(batchExecutionContextInstance);
        }
    }

    private void generateBatchExecutionWithParamInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select JOB_EXECUTION_ID, PARAMETER_NAME, PARAMETER_TYPE, " +
                "PARAMETER_VALUE, IDENTIFYING FROM " + prefix + "_JOB_EXECUTION_PARAMS");
        for (Map<String, Object> row : result) {
            String batchExecutionWithParams = "insert into " + prefix + "_JOB_EXECUTION_PARAMS (JOB_EXECUTION_ID, PARAMETER_NAME, PARAMETER_TYPE, " +
                    "PARAMETER_VALUE, IDENTIFYING) " +
                    "values ('" + row.get("JOB_EXECUTION_ID") + "'," +
                    "'" + row.get("PARAMETER_NAME") + "'," +
                    "'" + row.get("PARAMETER_TYPE") + "'," +
                    "'" + row.get("PARAMETER_VALUE") + "'," +
                    "'" + row.get("IDENTIFYING") + "'" +
                    ");\n";
            System.out.println(batchExecutionWithParams);
            writer.write(batchExecutionWithParams);
        }
    }

    private void generateTaskExecutionInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select END_TIME, ERROR_MESSAGE, EXIT_CODE, " +
                "EXIT_MESSAGE, EXTERNAL_EXECUTION_ID, LAST_UPDATED, PARENT_EXECUTION_ID, START_TIME, " +
                "TASK_EXECUTION_ID, TASK_NAME FROM " + prefix + "_EXECUTION");
        for (Map<String, Object> row : result) {
            String taskExecutionInsert = "insert into " + prefix + "_EXECUTION (END_TIME, ERROR_MESSAGE, EXIT_CODE, " +
                    "EXIT_MESSAGE, EXTERNAL_EXECUTION_ID, LAST_UPDATED, PARENT_EXECUTION_ID, START_TIME, " +
                    "TASK_EXECUTION_ID, TASK_NAME)" +
                    "values ('" + row.get("END_TIME") + "'," +
                    replaceNullWithNull(row.get("ERROR_MESSAGE")) + "," +
                    "'" + row.get("EXIT_CODE") + "'," +
                    replaceNullWithNull(row.get("EXIT_MESSAGE")) + "," +
                    replaceNullWithNull(row.get("EXTERNAL_EXECUTION_ID")) + "," +
                    "'" + row.get("LAST_UPDATED") + "'" + "," +
                    replaceNullWithNull(row.get("PARENT_EXECUTION_ID")) + "," +
                    "'" + row.get("START_TIME") + "'" + "," +
                    "'" + row.get("TASK_EXECUTION_ID") + "'" + "," +
                    "'" + row.get("TASK_NAME") + "'" +
                    ");\n";
            System.out.println(taskExecutionInsert);
            writer.write(taskExecutionInsert);
        }
    }

    private void generateTaskExecutionParamInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select TASK_EXECUTION_ID, TASK_PARAM " +
                "FROM " + prefix + "_EXECUTION_PARAMS");
        for (Map<String, Object> row : result) {
            String taskExecutionParamInsert = "insert into " + prefix + "_EXECUTION_PARAMS (TASK_EXECUTION_ID, TASK_PARAM)" +
                    " values ('" + row.get("TASK_EXECUTION_ID") + "'," +
                    "'" + row.get("TASK_PARAM") + "'" +
                    ");\n";
            System.out.println(taskExecutionParamInsert);
            writer.write(taskExecutionParamInsert);
        }
    }

    private void generateTaskBatchInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select TASK_EXECUTION_ID, JOB_EXECUTION_ID " +
                "FROM " + prefix + "_TASK_BATCH");
        for (Map<String, Object> row : result) {
            String taskBatchInsert = "insert into " + prefix + "_TASK_BATCH (TASK_EXECUTION_ID, JOB_EXECUTION_ID)" +
                    " values ('" + row.get("TASK_EXECUTION_ID") + "'," +
                    "'" + row.get("JOB_EXECUTION_ID") + "'" +
                    ");\n";
            System.out.println(taskBatchInsert);
            writer.write(taskBatchInsert);
        }
    }

    private void generateBatchStepExecutionContextInserts(BufferedWriter writer, JdbcTemplate template, String prefix) throws Exception {
        List<Map<String, Object>> result = template.queryForList("select SERIALIZED_CONTEXT, SHORT_CONTEXT, " +
                "STEP_EXECUTION_ID FROM " + prefix + "_STEP_EXECUTION_CONTEXT");
        for (Map<String, Object> row : result) {
            String batchStepExecutionContext = "insert into " + prefix + "_STEP_EXECUTION_CONTEXT (SERIALIZED_CONTEXT, " +
                    "SHORT_CONTEXT, STEP_EXECUTION_ID) values (" +
                    replaceNullWithNull(row.get("SERIALIZED_CONTEXT")) + "," +
                    "'" + row.get("SHORT_CONTEXT") + "'," +
                    "'" + row.get("STEP_EXECUTION_ID") + "'" +
                    ");\n";
            System.out.println(batchStepExecutionContext);
            writer.write(batchStepExecutionContext);
        }
    }

    private String replaceNullWithNull(Object value) {
        if (value == null) {
            return "NULL";
        }
        return "'" + value + "'";
    }

    protected void generateImportFile(Class clazz, String importFileName, String prefix, String databaseType, long startValue) throws Exception {
        generateImportFile(clazz, importFileName, prefix, databaseType, null, startValue);
    }

    protected void generateImportFile(Class clazz, String importFileName, String prefix, String databaseType, String param, long startValue) throws Exception {
        setTestSequenceToStartValue(startValue);
        if (param != null) {
            try {
                SpringApplication.run(clazz,
                        "--logging.level.org.springframework.cloud.task=DEBUG",
                        "--spring.datasource.password=" + mariaDB.getPassword(),
                        "--spring.datasource.username=" + mariaDB.getUsername(),
                        "--spring.datasource.url=" + mariaDB.getJdbcUrl(),
                        "--spring.datasource.driverClassName=org.mariadb.jdbc.Driver",
                        param);
            } catch (Exception exception) {
                System.out.println("Application failed to run.   This may have been by design.  Verify with test.");
            }
        } else {
            SpringApplication.run(clazz,
                    "--logging.level.org.springframework.cloud.task=DEBUG",
                    "--spring.datasource.password=" + mariaDB.getPassword(),
                    "--spring.datasource.username=" + mariaDB.getUsername(),
                    "--spring.datasource.url=" + mariaDB.getJdbcUrl(),
                    "--spring.datasource.driverClassName=org.mariadb.jdbc.Driver");
        }
        configureImportFile(importFileName, prefix, databaseType, startValue);
    }

    private void setTestSequenceToStartValue(long startValue) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        template.execute("ALTER SEQUENCE BATCH_JOB_SEQ START WITH " + startValue + ";");
        template.execute("ALTER SEQUENCE BATCH_STEP_EXECUTION_SEQ START WITH " + startValue + ";");
        template.execute("ALTER SEQUENCE BATCH_JOB_EXECUTION_SEQ START WITH " + startValue + ";");
        template.execute("ALTER SEQUENCE TASK_SEQ START WITH " + startValue + ";");
    }

    private void setSequences(BufferedWriter writer, long startIndex, String prefix, String databaseType) throws Exception {
        String batchPrefix = (prefix.equals("default") ? "BATCH" : prefix);
        String taskPrefix = (prefix.equals("default") ? "TASK" : prefix);
        if (databaseType.equals("POSTGRESQL")) {
            setPostgresSequences(writer, startIndex, taskPrefix, batchPrefix);
        }
        if (databaseType.equals("MYSQL") || databaseType.equals("MARIADB")) {
            setMariadbSequences(writer, startIndex, taskPrefix, batchPrefix);
        }
    }

    private void setPostgresSequences(BufferedWriter writer, long startValue, String taskPrefix, String batchPrefix) throws Exception {
        setGenericSequences(writer, startValue, taskPrefix, batchPrefix);
    }

    private void setMariadbSequences(BufferedWriter writer, long startValue, String taskPrefix, String batchPrefix) throws Exception {
        setGenericSequences(writer, startValue, taskPrefix, batchPrefix);
    }

    private void setGenericSequences(BufferedWriter writer, long startValue, String taskPrefix, String batchPrefix) throws Exception {
        writer.write("\n\nALTER SEQUENCE " + taskPrefix + "_SEQ START WITH " + startValue + "; \n");
        writer.write("ALTER SEQUENCE " + batchPrefix + "_JOB_SEQ START WITH " + startValue + "; \n");
        writer.write("ALTER SEQUENCE " + batchPrefix + "_STEP_EXECUTION_SEQ START WITH " + startValue + "; \n");
        writer.write("ALTER SEQUENCE " + batchPrefix + "_JOB_EXECUTION_SEQ START WITH " + startValue + "; \n");
    }
}
