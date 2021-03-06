package org.persistent.studentservice.service;

import java.util.List;

import org.persistent.studentservice.common.Student;

public interface StudentService {
	
	Student save(Student student);
	
	Student findById(Long studentId);
	
	boolean deleteStudentById(Long studentId);
	
	List<Student> saveAll(List<Student> students);

}
