package org.persistent.studentservice.controller;

import java.util.function.Consumer;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

@RestController
public class StudentController {
	
	private static final Logger LOGGER =  LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	@Autowired	
	private EurekaClient eurekaClient;
	@Value("${spring.application.name}")
	private String serviceName;

	@PostMapping(path = "/students", consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public ResponseEntity<Student> create(@RequestBody Student student) {
		LOGGER.info("Saving student with name :" + student.getStudentName() + " and age :" + student.getAge());		
		final Student savedStudent = studentService.save(student);
		return new ResponseEntity<>(savedStudent, HttpStatus.OK);
	}
	
	@GetMapping(path = "/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public ResponseEntity<Student> getById(@PathVariable("studentId") Long studentId) {
		LOGGER.info("Retrieving the stude with id :" + studentId);
		final Student savedStudent = studentService.findById(studentId);
		return new ResponseEntity<>(savedStudent, HttpStatus.OK);
	}
	
	@GetMapping(path = "/eureka", produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	public ResponseEntity<Boolean> eureka() {
		final Application application =  eurekaClient.getApplication(serviceName);
		application.getInstances().stream().forEach(new Consumer<InstanceInfo>() {
			@Override
			public void accept(InstanceInfo instanceInfo) {				
				LOGGER.info("@@@@@@Application group name :"+ instanceInfo.getAppGroupName());
				LOGGER.info("@@@@@@Application name :"+ instanceInfo.getAppName());
				LOGGER.info("@@@@@@IP :"+ instanceInfo.getIPAddr());
				LOGGER.info("@@@@@@Port :"+ instanceInfo.getPort());
			}
		});
		LOGGER.info("@@@@@@@Registered application name:"+ application.getName());
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}
	
}
