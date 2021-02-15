package org.persistent.studentservice.kafka.producer;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.domain.PublishingStatus;

public interface KafkaPublishingService {

	PublishingStatus publish(Student studnt);

}
