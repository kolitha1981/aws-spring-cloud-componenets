package org.persistent.studentservice.kafka.consumer;

import org.persistent.studentservice.common.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;

@EnableBinding(Sink.class)
public class StudentKafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentKafkaConsumer.class);
	
	@StreamListener(target = Sink.INPUT)
	public void consumeStudentPayLoads(@Payload Student student) {
		final String message = "@@@@@@Consuming student payload ith id :"+ student.getStudentId();
		LOGGER.info(message);
	}
}
