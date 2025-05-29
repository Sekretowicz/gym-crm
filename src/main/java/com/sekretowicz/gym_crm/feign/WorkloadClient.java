package com.sekretowicz.gym_crm.feign;

import com.sekretowicz.gym_crm.dto.workload.WorkloadRequestDto;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "workload-service")
public interface WorkloadClient {

    @PostMapping("/workload/")
    @CircuitBreaker(name = "workloadService", fallbackMethod = "doNothing")
    void notifyWorkload(@RequestBody WorkloadRequestDto dto);

    default void doNothing() {
        // This method is a placeholder for the default behavior when the circuit breaker is open.
        // It can be used to log or handle the situation gracefully.
    }

    @GetMapping("/workload/hello")
    void sayHello();
}