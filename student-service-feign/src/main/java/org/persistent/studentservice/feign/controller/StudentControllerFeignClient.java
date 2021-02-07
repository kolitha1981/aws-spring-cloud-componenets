package org.persistent.studentservice.feign.controller;

import org.persistent.studentservice.common.controller.AbstractStudentController;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${application.student.service.name}")
public interface StudentControllerFeignClient extends AbstractStudentController {

}
