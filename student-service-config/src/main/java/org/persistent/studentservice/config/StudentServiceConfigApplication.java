package org.persistent.studentservice.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigServer
@ComponentScan(basePackages = "org.persistent.studentservice.config")
public class StudentServiceConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceConfigApplication.class, args);
	}

}
