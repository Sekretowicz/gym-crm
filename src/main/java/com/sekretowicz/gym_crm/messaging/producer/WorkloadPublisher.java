package com.sekretowicz.gym_crm.messaging.producer;

import com.sekretowicz.gym_crm.messaging.config.JmsConfig;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkloadPublisher {

    private final JmsTemplate jmsTemplate;

    public void publish(WorkloadMessageDto dto) {
        jmsTemplate.convertAndSend(JmsConfig.WORKLOAD_QUEUE, dto);
    }
}