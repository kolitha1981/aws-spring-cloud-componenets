package org.persistent.studentservice.feign.controller;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "${application.student.service.name}")
public interface StudentFeignClient {

}
