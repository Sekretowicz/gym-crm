package com.sekretowicz.gym_crm.messaging.producer;

import com.sekretowicz.gym_crm.messaging.config.JmsConfig;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkloadPublisher {

    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(WorkloadMessageDto dto) {
        jmsTemplate.convertAndSend(JmsConfig.WORKLOAD_QUEUE, dto);
    }
}