package org.persistent.studentservice.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.persistent.studentservice.common.Student;
import org.persistent.studentservice.exception.handler.StudentExceptionHandler;
import org.persistent.studentservice.exceptions.StudentNotFoundException;
import org.persistent.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@EnableAutoConfiguration(exclude = SecurityAutoConfiguration.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, 
  classes = {StudentController.class, StudentExceptionHandler.class})
public class StudentControllerTest {
	
	@Autowired
    private MockMvc mvc;
	@MockBean
	private StudentService studentService;

    
    @Test
    public void testSaveStudent() throws Exception{
        final Student student =  new Student(1L, "Jane", 10);
        Mockito.doReturn(student).when(studentService).save(student);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mvc.perform(post("/students").contentType(MediaType.APPLICATION_JSON)
        		.content(mapper.writeValueAsBytes(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName", is("Jane")));        
    }
    
    @Test
    public void testGetStudentByIdForValidValue() throws Exception{
        final Student student =  new Student(1L, "Jane", 10);
        Mockito.doReturn(student).when(studentService).findById(1L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mvc.perform(get("/students/1").contentType(MediaType.APPLICATION_JSON))        		
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.studentName", is("Jane")));        
    }
    @Test
    public void testGetStudentByIdForInValidValue() throws Exception{
        Mockito.doThrow(StudentNotFoundException.class).when(studentService).findById(2L);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mvc.perform(get("/students/2").contentType(MediaType.APPLICATION_JSON))        		
                .andExpect(status().isNotFound());        
    }
    
}
