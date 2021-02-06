package org.persistent.studentservice.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.persistent.studentservice.exceptions.StudentNotFoundException;
import org.persistent.studentservice.model.Student;
import org.persistent.studentservice.repository.StudentRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;

@DataMongoTest(excludeAutoConfiguration = { EmbeddedMongoAutoConfiguration.class })
@ExtendWith(SpringExtension.class)
public class StudentServiceTest {

	@Autowired
	private StudentService studentService;

	@Test
	public void testSaveStudent() {
		final Student student = new Student(1L, "Jane", 20);
		final Student savedStudent = this.studentService.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
	}

	@Test
	public void testStudentFindByValidId() {
		final Student student = new Student(1L, "Jane", 20);
		final Student savedStudent = this.studentService.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
		final Student loadedStudent = this.studentService.findById(1L);
		assertNotNull(loadedStudent);
		assertNotNull(loadedStudent.getStudentId());
	}

	@Test
	public void testStudentFindByInValidId() {
		final Student student = new Student(1L, "Jane", 20);
		final Student savedStudent = this.studentService.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
		try {
			this.studentService.findById(2L);
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof StudentNotFoundException);
		}
	}

	@Configuration
	static class StudentTestConfig implements InitializingBean, DisposableBean {

		MongodExecutable executable;
		@Value("${org.persistent.studentservice.database.name}")
		private String databaseName;
		@Value("${org.persistent.studentservice.database.server}")
		private String server;
		@Value("${org.persistent.studentservice.database.server.port}")
		private int serverPort;

		@Override
		public void afterPropertiesSet() throws Exception {
			executable = MongodStarter.getDefaultInstance()
					.prepare(new MongodConfigBuilder().version(Version.Main.PRODUCTION)
							.net(new Net(server, serverPort, Network.localhostIsIPv6())).build());
			executable.start();
		}

		@Bean
		public MongoRepositoryFactoryBean mongoFactoryRepositoryBean() {
			final StringBuilder urlBuilder = new StringBuilder();
			urlBuilder.append("mongodb://");
			urlBuilder.append(server).append(":");
			urlBuilder.append(serverPort).append("/").append(databaseName);
			final MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
					.applyConnectionString(new ConnectionString(urlBuilder.toString())).build());
			final MongoTemplate template = new MongoTemplate(mongoClient, "databaseName");
			final MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(
					StudentRepository.class);
			mongoDbFactoryBean.setMongoOperations(template);
			return mongoDbFactoryBean;
		}

		@Bean
		public StudentService studentService(StudentRepository studentRepository) {
			return new StudentServiceImpl(studentRepository);
		}

		@Override
		public void destroy() throws Exception {
			executable.stop();
		}

	}

}
