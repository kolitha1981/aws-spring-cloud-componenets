package org.persistent.studentservice.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "org.persistent.studentservice.feign")
public class StudentServiceFeignApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentServiceFeignApplication.class, args);
	}

}
