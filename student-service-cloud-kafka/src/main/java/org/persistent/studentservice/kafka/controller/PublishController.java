package org.persistent.studentservice.kafka.controller;

import java.util.List;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.model.PublishingStatus;
import org.persistent.studentservice.kafka.service.KafkaPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublishController {

	@Autowired
	private KafkaPublishingService kafkaPublishingService;

	@PostMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<List<PublishingStatus>> publish(@RequestBody List<Student> students) {
		final List<PublishingStatus> publishingStatus = kafkaPublishingService.publish(students);
		return new ResponseEntity<>(publishingStatus, HttpStatus.OK);
	}

}
