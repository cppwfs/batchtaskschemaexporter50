package io.spring.batchschema;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mariadb.jdbc.MariaDbDataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class Batchschema30ApplicationTests {
	@Container
	private static final MariaDBContainer mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));
	private static MariaDbDataSource dataSource;

	@BeforeAll
	static void setUp() throws Exception{
			dataSource = new MariaDbDataSource();
			dataSource.setUser(mariaDB.getUsername());
			dataSource.setPassword(mariaDB.getPassword());
			dataSource.setUrl(mariaDB.getJdbcUrl());
			ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
			databasePopulator.addScript(new ClassPathResource("/org/springframework/batch/core/schema-mariadb.sql"));
			databasePopulator.addScript(new ClassPathResource("/org/springframework/cloud/task/schema-mariadb.sql"));
			databasePopulator.addScript(new FileSystemResource("batchloadfiles/singleJobSingleStepJobParam.load"));
			databasePopulator.execute(dataSource);
	}


	@Test
	void testLoadDB() {
		JdbcTemplate template  = new JdbcTemplate(dataSource);
		List<Map<String,Object>> result = template.queryForList("select count(*) as mycount from BATCH_JOB_INSTANCE");
		assertEquals(1L, result.get(0).get("mycount"));
		result = template.queryForList("select count(*) as mycount from BATCH_STEP_EXECUTION");
		assertEquals(1L, result.get(0).get("mycount"));
		result = template.queryForList("select count(*) as mycount from TASK_EXECUTION");
		assertEquals(1L, result.get(0).get("mycount"));
	}

}
