package org.persistent.studentservice.kafka.model;

public class PublishingStatus {
	
	private String studentPayLoad;
	private Status status;
	
	public PublishingStatus(String studentPayLoad, Status status) {
		super();
		this.studentPayLoad = studentPayLoad;
		this.status = status;
	}

	public String getStudentPayLoad() {
		return studentPayLoad;
	}

	public Status getStatus() {
		return status;
	}	

}
