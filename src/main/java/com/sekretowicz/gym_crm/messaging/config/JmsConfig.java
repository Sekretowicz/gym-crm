package com.sekretowicz.gym_crm.messaging.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import jakarta.jms.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.Map;

@Configuration
@EnableJms
public class JmsConfig {
    public static final String WORKLOAD_QUEUE = "workload.queue";

    @Bean
    public ConnectionFactory sqsConnectionFactory() {
        SqsClient sqs = SqsClient.builder()
                .region(Region.EU_NORTH_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        return new SQSConnectionFactory(new ProviderConfiguration(), sqs);
    }

    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        converter.setTargetType(MessageType.TEXT);
        converter.setObjectMapper(objectMapper);

        converter.setTypeIdPropertyName("_type");

        converter.setTypeIdMappings(Map.of(
                "Workload", WorkloadMessageDto.class
        ));

        return converter;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory, MessageConverter converter) {
        JmsTemplate template = new JmsTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter messageConverter
    ) {
        var f = new DefaultJmsListenerContainerFactory();
        f.setConnectionFactory(connectionFactory);
        f.setMessageConverter(messageConverter);

        f.setSessionTransacted(false);

        f.setSessionAcknowledgeMode(jakarta.jms.Session.AUTO_ACKNOWLEDGE);

        return f;
    }
}