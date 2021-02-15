package org.persistent.studentservice.kafka.controller;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.domain.PublishingStatus;
import org.persistent.studentservice.kafka.producer.KafkaPublishingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class KakaPublishController {

	private KafkaPublishingService kafkaPublishingService;

	@PostMapping(path = "/publish")
	public ResponseEntity<PublishingStatus> publish(@RequestBody Student student) {
		final PublishingStatus publishingStatus = this.kafkaPublishingService.publish(student);
		if (publishingStatus == PublishingStatus.STATUS_FAILED) {
			return new ResponseEntity<>(publishingStatus, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(publishingStatus, HttpStatus.OK);
	}

}
