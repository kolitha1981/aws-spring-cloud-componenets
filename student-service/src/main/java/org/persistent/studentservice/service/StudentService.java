package org.persistent.studentservice.service;

import org.persistent.studentservice.common.Student;

public interface StudentService {
	
	Student save(Student student);
	
	Student findById(Long studentId);

}
