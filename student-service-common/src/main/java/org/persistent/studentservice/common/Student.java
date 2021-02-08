package org.persistent.studentservice.common;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document(collection = "students")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Student{

	@Id @EqualsAndHashCode.Exclude
	@ApiModelProperty(notes = "The id of the student.")
	private Long studentId;
	@ApiModelProperty(notes = "The name of the student.")
	private String studentName;
	@ApiModelProperty(notes = "The age of the student")
	private int age;
	
}
