package org.persistent.studentservice.feign.controller;

import org.persistent.studentservice.common.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableFeignClients
public class StudentFeignController {

	@Autowired
	private StudentControllerFeignClient studentFeignClient;

	@PostMapping(path = "/feign/students", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Student> create(@RequestBody Student student) {
		return studentFeignClient.create(student);
	}

	@GetMapping(path = "/feign/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Student> getById(@PathVariable("studentId") Long studentId) {
		return studentFeignClient.getById(studentId);
	}

}
