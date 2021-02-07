package org.persistent.studentservice.feign.controller;

import org.persistent.studentservice.model.Student;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${application.student.service.name}")
public interface StudentFeignClient {

	ResponseEntity<Student> getById(Long studentId);
}
