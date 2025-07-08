package com.sekretowicz.gym_crm.steps;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto.training.TrainingResponse;
import com.sekretowicz.gym_crm.messaging.config.JmsConfig;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.repo.TrainingRepo;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.utils.ScenarioContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jms.core.JmsTemplate;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class TrainingSteps {
    @Autowired
    private ScenarioContext scenarioContext;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private TrainingRepo trainingRepo;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;

    @When("Test data is prepared")
    public void testDataIsPrepared() {
        scenarioContext.prepareTestData();
    }

    @When("I arrange a training session")
    public void whenICreateTraining() {
        AddTrainingRequest request = new AddTrainingRequest(
                scenarioContext.getCurrentTrainee().getUsername(),
                scenarioContext.getCurrentTrainer().getUsername(),
                "Fitness",
                LocalDate.now(),
                60
        );

        //Generate JWT token for the current trainer
        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(scenarioContext.getJwtTokenForCurrentUser());
        headers.setContentType(MediaType.APPLICATION_JSON);


        //Sending request to controller, saving returned object in scenario context if OK
        ResponseEntity<Void> response = restTemplate.postForEntity("/trainings", new HttpEntity<>(request, headers), Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected response status to be OK");
        scenarioContext.setCurrentTraining(request);
    }

    @Then("Training session appears in ActiveMQ")
    public void trainingSessionAppearsInActiveMQ() {

        //Print current stuff
        System.out.println("Current user: " + scenarioContext.getCurrentTrainer().getUsername());
        System.out.println("Current user: " + scenarioContext.getCurrentTrainer().getFirstName());
        System.out.println("Current user: " + scenarioContext.getCurrentTrainer().getLastName());

        WorkloadMessageDto expectedMessage = new WorkloadMessageDto(
                scenarioContext.getCurrentTrainer().getUsername(),
                scenarioContext.getCurrentTrainer().getFirstName(),
                scenarioContext.getCurrentTrainer().getLastName(),
                scenarioContext.getCurrentTrainer().getIsActive(),
                scenarioContext.getCurrentTraining().getTrainingDate(),
                scenarioContext.getCurrentTraining().getTrainingDuration(),
                "ADD"
        );
        WorkloadMessageDto messageFromMQ = (WorkloadMessageDto)jmsTemplate.receiveAndConvert(JmsConfig.WORKLOAD_QUEUE);
        assertEquals(expectedMessage, messageFromMQ, "Expected message from ActiveMQ to match the created training session");
    }

}
