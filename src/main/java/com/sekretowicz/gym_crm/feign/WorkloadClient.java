package com.sekretowicz.gym_crm.feign;

import com.sekretowicz.gym_crm.dto.workload.WorkloadRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload-service", configuration = FeignConfig.class)
public interface WorkloadClient {

    @PostMapping("/workload")
    void notifyWorkload(@RequestBody WorkloadRequestDto dto);

    @GetMapping("/workload/hello")
    void sayHello();
}