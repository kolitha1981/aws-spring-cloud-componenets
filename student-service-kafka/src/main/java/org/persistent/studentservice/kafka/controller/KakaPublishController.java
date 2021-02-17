package org.persistent.studentservice.kafka.controller;

import java.util.List;
import java.util.Map;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.domain.PublishingStatus;
import org.persistent.studentservice.kafka.producer.KafkaPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KakaPublishController {

	@Autowired
	private KafkaPublishingService kafkaPublishingService;

	@PostMapping(path = "/publish", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<Map<Long, PublishingStatus>> publish(@RequestBody List<Student> students) {
		final Map<Long, PublishingStatus> publishingStatuses = this.kafkaPublishingService.publish(students);
		return new ResponseEntity<>(publishingStatuses, HttpStatus.OK);
	}

}
