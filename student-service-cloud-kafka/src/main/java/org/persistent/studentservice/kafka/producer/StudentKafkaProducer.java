package org.persistent.studentservice.kafka.producer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;

@EnableBinding(Source.class)
public class StudentKafkaProducer {
	
	private Source sinkSource;

	public StudentKafkaProducer(Source sinkSource) {
		super();
		this.sinkSource = sinkSource;
	}

	public Source getSinkSource() {
		return sinkSource;
	}

	public void setSinkSource(Source sinkSource) {
		this.sinkSource = sinkSource;
	}	

}
