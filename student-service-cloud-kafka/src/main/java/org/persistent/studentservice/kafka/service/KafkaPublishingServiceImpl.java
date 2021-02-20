package org.persistent.studentservice.kafka.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.kafka.constants.StudentMessageHeaderConstants;
import org.persistent.studentservice.kafka.model.PublishingStatus;
import org.persistent.studentservice.kafka.model.Status;
import org.persistent.studentservice.kafka.producer.StudentKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaPublishingServiceImpl implements KafkaPublishingService {

	private StudentKafkaProducer studentKafkaProducer;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPublishingServiceImpl.class);

	@Autowired
	public KafkaPublishingServiceImpl(final StudentKafkaProducer studentKafkaProducer) {
		this.studentKafkaProducer = studentKafkaProducer;
	}

	@Override
	public List<PublishingStatus> publish(List<Student> students) {
		final List<PublishingStatus> publishingStatus = new ArrayList<PublishingStatus>();
		students.stream().forEach((student) -> {
			String studentJsonPayLoad = null;
			try {
				studentJsonPayLoad = new ObjectMapper().writer().writeValueAsString(student);
				studentKafkaProducer.getSinkSource().output().send(MessageBuilder.withPayload(student)
						.setHeader(StudentMessageHeaderConstants.MESSAGE_KEY, student.getStudentId())
						.setHeader(StudentMessageHeaderConstants.CREATED_BY, "adminstrator")
						.setHeader(StudentMessageHeaderConstants.CREATED_ON, Calendar.getInstance().getTime()).build());
                LOGGER.info("@@@@Successfully published the message having student id :"+ student.getStudentId()); 
				publishingStatus.add(new PublishingStatus(studentJsonPayLoad, Status.PENDING));
			} catch (Exception e) {
				LOGGER.info("@@@@Failed piblishing the message having student id :"+ student.getStudentId());
				publishingStatus.add(new PublishingStatus(studentJsonPayLoad, Status.ERROR));
			}
		});
		return publishingStatus;
	}

}
