package org.persistent.studentservice.kafka.producer;

import java.util.List;
import java.util.Map;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.domain.PublishingStatus;

public interface KafkaPublishingService {

	Map<Long,PublishingStatus> publish(List<Student> student);

}
