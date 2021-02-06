package org.persistent.studentservice.repository;

import org.persistent.studentservice.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentRepository extends MongoRepository<Student, Long> {

}
