package org.persistent.studentservice.controller;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.common.controller.AbstractStudentController;
import org.persistent.studentservice.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RefreshScope
@Api(value = "StudentuService", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController implements AbstractStudentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentService studentService;
	@Autowired
	private EurekaClient eurekaClient;
	@Value("${spring.application.name}")
	private String serviceName;	

	@ApiOperation(value = "Creates a student instance.", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created the student."),
			@ApiResponse(code = 500, message = "An internal error has occurred.") })
	@PostMapping(path = "/students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Student> create(@RequestBody Student student) {
		LOGGER.info("Saving student with name :" + student.getStudentName() + " and age :" + student.getAge());
		final Student savedStudent = studentService.save(student);
		return new ResponseEntity<>(savedStudent, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Creates multiple student instances.", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully created the student instances."),
			@ApiResponse(code = 500, message = "An internal error has occurred.") })
	@PostMapping(path = "/students/batch", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<Student>> createAsBatch(@RequestBody List<Student> students) {
		final List<String> studentIds = students.stream().map((student) ->  {
				return String.valueOf(student.getStudentId());
			}).collect(Collectors.toList());
		LOGGER.info("Saving students with ids :" + String.join(",", studentIds));
		final List<Student> savedStudents = studentService.saveAll(students);
		return new ResponseEntity<>(savedStudents, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves a student for a given Id.", response = ResponseEntity.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the student."),
			@ApiResponse(code = 500, message = "An internal error has occurred.") })
	@GetMapping(path = "/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Student> getById(@PathVariable("studentId") Long studentId) {
		LOGGER.info("Retrieving the stude with id :" + studentId);
		final Student savedStudent = studentService.findById(studentId);
		return new ResponseEntity<>(savedStudent, HttpStatus.OK);
	}

	@GetMapping(path = "/eureka", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Boolean> eureka() {
		final Application application = eurekaClient.getApplication(serviceName);
		application.getInstances().stream().forEach(new Consumer<InstanceInfo>() {
			@Override
			public void accept(InstanceInfo instanceInfo) {
				LOGGER.info("@@@@@@Application group name :" + instanceInfo.getAppGroupName());
				LOGGER.info("@@@@@@Application name :" + instanceInfo.getAppName());
				LOGGER.info("@@@@@@IP :" + instanceInfo.getIPAddr());
				LOGGER.info("@@@@@@Port :" + instanceInfo.getPort());
			}
		});
		LOGGER.info("@@@@@@@Registered application name:" + application.getName());
		return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
	}

}
