package com.sekretowicz.gym_crm.messaging.producer;

import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class WorkloadPublisher {

    //Previously it was JmsTemplate
    @Autowired
    private JmsTemplate jmsTemplate;

    public void publish(WorkloadMessageDto dto) {
        //sqsTemplate.convertAndSend(JmsConfig.WORKLOAD_QUEUE, dto);
        //sqsTemplate.send("gym-crm-queue", dto);
        jmsTemplate.convertAndSend("gym-crm-queue", dto);
    }
}