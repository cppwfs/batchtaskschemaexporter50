package io.spring.batchschema;


import org.junit.jupiter.api.BeforeEach;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.*;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.*;


@Testcontainers
public abstract class AbstractBatchExport {
    private static final String CONTAINER_BATCH_LOAD_FILE_NAME = "batchPreLoad.txt";

    @Container
    public static final MariaDBContainer mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @BeforeEach
    void setUp() throws Exception{
        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setUser(mariaDB.getUsername());
        dataSource.setPassword(mariaDB.getPassword());
        dataSource.setUrl(mariaDB.getJdbcUrl());
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-mariadb.sql"));

        databasePopulator.execute(dataSource);
    }


    public void configureImportFile(String nameOfFile, String prefix, String databaseName) throws Exception{
        System.out.println(mariaDB.execInContainer("/usr/bin/mariadb-dump", "--password=test", "--user=test", "--all-databases", "--no-create-info", "--no-create-db", "--skip-comments", "--result-file=/usr/bin/" + CONTAINER_BATCH_LOAD_FILE_NAME));
        mariaDB.copyFileFromContainer("/usr/bin/" + CONTAINER_BATCH_LOAD_FILE_NAME, "./" + CONTAINER_BATCH_LOAD_FILE_NAME);
        ResourceLoader resourceLoader = new FileSystemResourceLoader();
        Resource resource = resourceLoader.getResource("./" + CONTAINER_BATCH_LOAD_FILE_NAME);
        Resource outResource = resourceLoader.getResource("./batchloadfiles/" + nameOfFile);
        WritableResource writableResource = (WritableResource) outResource;
        InputStream in = resource.getInputStream();
        OutputStream out = writableResource.getOutputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
        try {
            writer.write("\n\n SET FOREIGN_KEY_CHECKS=0; \n\n");
            while (true) {
                String line = reader.readLine();
                if (line == null)
                    break;
                if (line.equals("USE `test`;")) {
                    line = "USE `" + databaseName + "`;";
                }
                writer.write(line + "\n");
            }
            addPrefix(prefix, writer);
            writer.write("\n\n SET FOREIGN_KEY_CHECKS=1; \n\n");
        }
        finally {
            reader.close();
            writer.close();
        }
    }

    private void addPrefix(String prefix, BufferedWriter writer) throws Exception{
        if(prefix.equals("default")) {
            return;
        }
        writer.write("ALTER TABLE BATCH_JOB_EXECUTION RENAME TO " + prefix + "_JOB_EXECUTION;\n");
        writer.write("ALTER TABLE BATCH_JOB_EXECUTION_CONTEXT RENAME TO " + prefix + "_JOB_EXECUTION_CONTEXT;\n");
        writer.write("ALTER TABLE BATCH_JOB_EXECUTION_PARAMS RENAME TO " + prefix + "_JOB_EXECUTION_PARAMS;\n");
        writer.write("ALTER TABLE BATCH_JOB_EXECUTION_SEQ RENAME TO " + prefix + "_JOB_EXECUTION_SEQ;\n");
        writer.write("ALTER TABLE BATCH_JOB_INSTANCE RENAME TO " + prefix + "_JOB_INSTANCE;\n");
        writer.write("ALTER TABLE BATCH_JOB_SEQ RENAME TO " + prefix + "_JOB_SEQ;\n");
        writer.write("ALTER TABLE BATCH_STEP_EXECUTION RENAME TO " + prefix + "_STEP_EXECUTION;\n");
        writer.write("ALTER TABLE BATCH_STEP_EXECUTION_CONTEXT RENAME TO " + prefix + "_STEP_EXECUTION_CONTEXT;\n");
        writer.write("ALTER TABLE BATCH_STEP_EXECUTION_SEQ RENAME TO " + prefix + "_STEP_EXECUTION_SEQ;\n");
        writer.write("ALTER TABLE TASK_EXECUTION RENAME TO " + prefix + "_EXECUTION;\n");
        writer.write("ALTER TABLE TASK_EXECUTION_PARAMS RENAME TO " + prefix + "_EXECUTION_PARAMS;\n");
        writer.write("ALTER TABLE TASK_LOCK RENAME TO " + prefix + "_LOCK;\n");
        writer.write("ALTER TABLE TASK_TASK_BATCH RENAME TO " + prefix + "_TASK_BATCH;\n");
        writer.write("ALTER TABLE TASK_SEQ RENAME TO " + prefix + "_SEQ;\n");

    }

    protected void generateImportFile(Class clazz, String importFileName, String prefix, String databaseName) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(clazz,
                "--logging.level.org.springframework.cloud.task=DEBUG",
                "--spring.datasource.password=" + mariaDB.getPassword(),
                "--spring.datasource.username=" + mariaDB.getUsername(),
                "--spring.datasource.url=" + mariaDB.getJdbcUrl(),
                "--spring.datasource.driverClassName=org.mariadb.jdbc.Driver");
        configureImportFile(importFileName, prefix, databaseName);
    }
}
