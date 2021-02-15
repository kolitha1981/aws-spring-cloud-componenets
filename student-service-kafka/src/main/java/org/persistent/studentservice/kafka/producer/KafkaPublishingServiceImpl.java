package org.persistent.studentservice.kafka.producer;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.domain.PublishingStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaPublishingServiceImpl implements KafkaPublishingService {

	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPublishingServiceImpl.class);
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Value(value = "${kafka.student.message.topic.name}")
	private String topicName;

	@Override
	public PublishingStatus publish(Student studnt){
		LOGGER.info("Publishing student with id :" + studnt.getStudentId() + " tp topic :" + topicName);
		final ObjectMapper objectMapper = new ObjectMapper();
		try {
			final String studentJson = objectMapper.writer().writeValueAsString(studnt);
			final ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, studentJson);
			future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
				@Override
				public void onSuccess(SendResult<String, String> result) {
					final String successMessage = "Sent message=[" + studentJson + "] " + "with offset=["
							+ result.getRecordMetadata().offset() + "]";
					LOGGER.info(successMessage);
				}

				@Override
				public void onFailure(Throwable ex) {
					LOGGER.info("Unable to send message=[" + studentJson + "] due to : " + ex.getMessage());
				}
			});
		} catch (Exception e) {
			LOGGER.info("Error when processing student message:" + e.getMessage());
			return PublishingStatus.STATUS_FAILED;
		}
		return PublishingStatus.STATUS_PENDING;
	}

}
