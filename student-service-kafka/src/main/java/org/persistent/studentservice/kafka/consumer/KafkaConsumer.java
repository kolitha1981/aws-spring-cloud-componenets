package org.persistent.studentservice.kafka.consumer;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

	private CountDownLatch latch = new CountDownLatch(3);

	private CountDownLatch partitionLatch = new CountDownLatch(2);

	/*
	@KafkaListener(topics = "student-messages",containerFactory = "kafkaListenerContainerFactory")
	public void listenWithHeaders(@Payload String messagePayLoad, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.MESSAGE_KEY) Long messageKey, @Header(KafkaHeaders.TIMESTAMP) String timestamp) {
		final Date messageTimeStamp = new Date(Long.valueOf(timestamp));
		String notoficationMessage = "######Received Message: " + messagePayLoad + "from partition: " + partition
				+ " with message key :" + messageKey;
		LOGGER.info("Logged at :" + messageTimeStamp);
		LOGGER.info(notoficationMessage);
		latch.countDown();
	}

	@KafkaListener(topicPartitions = @TopicPartition(topic = "student-messages", 
			partitions = { "0","1","2" }), containerFactory = "kafkaListenerContainerFactory")
	public void listenToPartition(@Payload String messagePayLoad, 
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
			@Header(KafkaHeaders.MESSAGE_KEY) Long messageKey, 
			@Header(KafkaHeaders.TIMESTAMP) String timestamp) {
		final Date messageTimeStamp = new Date(Long.valueOf(timestamp));
		String notoficationMessage = "########Received Message: " + messagePayLoad + "from partition: " + partition
				+ " with message key :" + messageKey;
		LOGGER.info("Logged at :" + messageTimeStamp);
		LOGGER.info(notoficationMessage);
		this.partitionLatch.countDown();
	} */
	
	@KafkaListener(topicPartitions = @TopicPartition(topic = "student-messages", 
			partitions = { "0","1","2" }), containerFactory = "kafkaListenerContainerFactory")
	public void listenToTopic(@Payload String studentPayLoad,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
		String notoficationMessage = "########Received student with id: " + studentPayLoad 
				+ "from partition:"+ partition ;
		LOGGER.info(notoficationMessage);
		this.partitionLatch.countDown();
	}

}
