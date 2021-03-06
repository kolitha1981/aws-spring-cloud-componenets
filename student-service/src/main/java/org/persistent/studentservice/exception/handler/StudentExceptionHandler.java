package org.persistent.studentservice.exception.handler;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.persistent.studentservice.exceptions.InvalidBatchCountException;
import org.persistent.studentservice.exceptions.StudentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class StudentExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(StudentNotFoundException.class)
	public void handleStudentNotFound(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.NOT_FOUND.value());
	}

	@ExceptionHandler(InvalidBatchCountException.class)
	public void handleInvalidBatchCount(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value());
	}

}
