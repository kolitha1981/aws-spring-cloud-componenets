package org.persistent.studentservice.kafka.service;

import java.util.List;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.model.PublishingStatus;

public interface KafkaPublishingService {
	
	List<PublishingStatus> publish(List<Student> students);

}
