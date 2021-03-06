package org.persistent.studentservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.exceptions.InvalidBatchCountException;
import org.persistent.studentservice.exceptions.StudentNotFoundException;
import org.persistent.studentservice.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@RefreshScope
@Service
public class StudentServiceImpl implements StudentService {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentServiceImpl.class);

	private StudentRepository studentRepository;

	@Value("${student.service.save.batch.size}")
	private int saveBatchSize;

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
	public List<Student> saveAll(List<Student> students) {
		if (students.size() > saveBatchSize) {
			throw new InvalidBatchCountException("Batch count must be : " + saveBatchSize);
		}
		final List<String> studentIds = students.stream().map((student) -> {
			return String.valueOf(student.getStudentId());
		}).collect(Collectors.toList());
		LOGGER.info("Saving student with ids :" + String.join(",", studentIds));
		return studentRepository.saveAll(students);
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
