package org.persistent.studentservice.kafka.consumer;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.persistent.studentservice.common.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private CountDownLatch latch = new CountDownLatch(3);

	private CountDownLatch partitionLatch = new CountDownLatch(2);

	@KafkaListener(topics = "${kafka.student.message.topic.name}", 
			groupId = "${kafka.student.consumer.group.id}")
	public void listenGroupFoo(String message) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final Student student = objectMapper.reader().readValue(message, Student.class);
			String studentLog = "@@@@ Consumed student message with id : " + student.getStudentId() + ".";
			LOGGER.info(studentLog);
		} catch (IOException e) {
			String errorMessage = "Error in processing the consomed student message:" + e.getMessage();
			LOGGER.info(errorMessage);
		}
		String notoficationMessage = "@@@@@Received Message in group foo: " + message;
		LOGGER.info(notoficationMessage);
	}

	@KafkaListener(topics = "${kafka.student.message.topic.name}")
	public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.MESSAGE_KEY) String messageKey, @Header(KafkaHeaders.TIMESTAMP) String timestamp) {
		final Date messageTimeStamp = new Date(Long.valueOf(timestamp));
		String notoficationMessage = "@@@@@Received Message: " + message + "from partition: " + partition
				+ " with message key :" + messageKey;
		LOGGER.info("Logged at :" + messageTimeStamp);
		LOGGER.info(notoficationMessage);
		latch.countDown();
	}

	@KafkaListener(topicPartitions = @TopicPartition(topic = "${kafka.student.message.topic.name}", 
			partitions = { "0","1","2" }))
	public void listenToPartition(@Payload String message, 
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.MESSAGE_KEY) String messageKey, 
			@Header(KafkaHeaders.TIMESTAMP) String timestamp) {
		final Date messageTimeStamp = new Date(Long.valueOf(timestamp));
		String notoficationMessage = "@@@@@Received Message: " + message + "from partition: " + partition
				+ " with message key :" + messageKey;
		LOGGER.info("Logged at :" + messageTimeStamp);
		LOGGER.info(notoficationMessage);
		this.partitionLatch.countDown();
	}

}
