package org.persistent.studentservice.service;

import org.persistent.studentservice.model.Student;

public interface StudentService {
	
	Student save(Student student);
	
	Student findById(Long studentId);

}
