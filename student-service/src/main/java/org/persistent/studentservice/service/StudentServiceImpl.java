package org.persistent.studentservice.service;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.exceptions.StudentNotFoundException;
import org.persistent.studentservice.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

	private StudentRepository studentRepository;

	@Autowired
	public StudentServiceImpl(final StudentRepository studentRepository) {
		this.studentRepository = studentRepository;
	}

	@Override
	public Student save(Student student) {
		LOGGER.info("Saving student with name :" + student.getStudentName() + " and age :" + student.getAge());
		return studentRepository.save(student);
	}

	@Override
	public Student findById(Long studentId) {
		LOGGER.info("Retrieving student with id :" + studentId);
		return studentRepository.findById(studentId).orElseThrow(() -> {
			return new StudentNotFoundException("Student not found for id :" + studentId);
		});
	}

	@Override
	public boolean deleteStudentById(Long studentId) {
		Student student = this.studentRepository.findById(studentId).orElseThrow(() -> {
			return new StudentNotFoundException("Student not found for id :" + studentId);
		});
		try {
			this.studentRepository.delete(student);
		} catch (Exception e) {
			LOGGER.info("Error when deleting student with id:" + studentId);
			return false;
		}
		return true;
	}

}
