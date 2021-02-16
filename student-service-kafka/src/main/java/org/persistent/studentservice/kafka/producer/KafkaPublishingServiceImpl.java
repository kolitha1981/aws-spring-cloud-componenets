package org.persistent.studentservice.kafka.producer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;import java.util.stream.Collector;
import java.util.stream.Collectors;

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
	public Map<Long, PublishingStatus> publish(List<Student> students) {
        students.stream().map(new Function<Student, String>() {

			@Override
			public String apply(Student student) {
				// TODO Auto-generated method stub
				return String.valueOf(student.getStudentId());
			}
		}).collect(Collectors.toList());
		LOGGER.info("Publishing student with id :" + "" + " tp topic :" + topicName);
		final ObjectMapper objectMapper = new ObjectMapper();
		final Map<Long, PublishingStatus> publishingStatuses = new HashMap<>();
		students.forEach(student -> {
			try {
				final Long partition = student.getStudentId() % 3;
				final String studentJson = objectMapper.writer().writeValueAsString(student);
				final ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(studentJson,
						partition.intValue(), System.currentTimeMillis(), String.valueOf(student.getStudentId()),
						studentJson);
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
				publishingStatuses.put(student.getStudentId(), PublishingStatus.STATUS_PENDING);
			} catch (Exception e) {
				LOGGER.info("Error when processing student message:" + e.getMessage());
				publishingStatuses.put(student.getStudentId(), PublishingStatus.STATUS_FAILED);
			}
		});
		return publishingStatuses;
	}

}
