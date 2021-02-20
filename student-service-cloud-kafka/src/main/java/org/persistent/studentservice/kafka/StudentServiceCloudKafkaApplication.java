package org.persistent.studentservice.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.persistent.studentservice.kafka")
public class StudentServiceCloudKafkaApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceCloudKafkaApplication.class, args);
	}

}
