package org.persistent.studentservice.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class StudentServiceDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceDiscoveryApplication.class, args);
	}

}
