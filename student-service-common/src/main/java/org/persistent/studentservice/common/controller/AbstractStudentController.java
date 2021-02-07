package org.persistent.studentservice.common.controller;

import org.persistent.studentservice.common.Student;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface AbstractStudentController {
	
	@PostMapping(path="/students" , consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<Student> create(@RequestBody Student student);
	
	@GetMapping(path = "/students/{studentId}", produces = MediaType.APPLICATION_JSON_VALUE )
	@ResponseBody
	ResponseEntity<Student> getById(@PathVariable("studentId") Long studentId);

}
