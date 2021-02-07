package org.persistent.studentservice.repository;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.persistent.studentservice.common.Student;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

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

public class StudentRepositoryTest {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Test
	public void testSaveStudent() {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		final Student student =  new Student(1L,"Jane_Doe", 20);
		final Student savedStudent = this.studentRepository.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
	}
	
	@Test
	public void testSaveStudentWithInvalidConstraints() {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		final Student student =  new Student(1L,"Jane", 20);
		final Student savedStudent = this.studentRepository.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
	}
	
	@Test
	public void testGetStudentByIdForGivenValidId() {
		final Student student =  new Student(1L,"Jane", 20);
		final Student savedStudent = this.studentRepository.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
		final Optional<Student> retrievedStudent = this.studentRepository.findById(1L);
		assertTrue(retrievedStudent.isPresent());
		assertNotNull(retrievedStudent.get());
	}
	
	@Test
	public void testGetStudentByIdForGivenInValidId() {
		final Student student =  new Student(1L,"Jane", 20);
		final Student savedStudent = this.studentRepository.save(student);
		assertNotNull(savedStudent);
		assertNotNull(savedStudent.getStudentId());
		Optional<Student> retrievedStudent = this.studentRepository.findById(2L);
		assertFalse(retrievedStudent.isPresent());
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
			executable = MongodStarter.getDefaultInstance().prepare(new MongodConfigBuilder()
					.version(Version.Main.PRODUCTION).net(new Net(server, serverPort, Network.localhostIsIPv6())).build());
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
			final MongoRepositoryFactoryBean mongoDbFactoryBean = new MongoRepositoryFactoryBean(StudentRepository.class);
			mongoDbFactoryBean.setMongoOperations(template);
			return mongoDbFactoryBean;
		}
		
		@Bean
		public ValidatingMongoEventListener validatingMongoEventListener() {
			return new ValidatingMongoEventListener(validator());
		}

		@Bean
		public LocalValidatorFactoryBean validator() {
			return new LocalValidatorFactoryBean();
		}

		@Override
		public void destroy() throws Exception {
			executable.stop();
		}

	}

}
