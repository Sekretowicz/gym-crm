package com.sekretowicz.gym_crm.steps;

import com.sekretowicz.gym_crm.dto.training.AddTrainingRequest;
import com.sekretowicz.gym_crm.dto.training.VerboseTrainingResponse;
import com.sekretowicz.gym_crm.messaging.config.JmsConfig;
import com.sekretowicz.gym_crm.messaging.dto.WorkloadMessageDto;
import com.sekretowicz.gym_crm.model.Training;
import com.sekretowicz.gym_crm.repo.TrainingRepo;
import com.sekretowicz.gym_crm.service.TrainingService;
import com.sekretowicz.gym_crm.utils.ScenarioContext;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.jms.Message;
import jakarta.transaction.Transactional;
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

        //Hack to clear ActiveMQ queue
        jmsTemplate.setReceiveTimeout(100);
        while (true) {
            Message message = jmsTemplate.receive(JmsConfig.WORKLOAD_QUEUE);
            if (message == null) break;
        }
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

        System.out.println("Current trainer: " + scenarioContext.getCurrentTrainer());
        System.out.println("Current trainee: " + scenarioContext.getCurrentTrainee());

        //Sending request to controller, saving returned object in scenario context if OK
        ResponseEntity<VerboseTrainingResponse> response = restTemplate.postForEntity(
                "/trainings",
                new HttpEntity<>(request, scenarioContext.getHeadersForCurrentUser()),
                VerboseTrainingResponse.class);
        assertEquals(HttpStatus.OK, response.getStatusCode(), "Expected response status to be OK");
        scenarioContext.setCurrentTraining(response.getBody());
    }

    @Then("Message appears in ActiveMQ")
    public void trainingSessionAppearsInActiveMQ() {
        //ScenarioContext
        System.out.println("Trainer: " + scenarioContext.getCurrentTrainer());
        System.out.println("Trainee: " + scenarioContext.getCurrentTrainee());
        System.out.println("Training: " + scenarioContext.getCurrentTraining());

        //Creating expected message from saved response
        WorkloadMessageDto expectedMessage = new WorkloadMessageDto(scenarioContext.getCurrentTraining());
        WorkloadMessageDto messageFromMQ = (WorkloadMessageDto)jmsTemplate.receiveAndConvert(JmsConfig.WORKLOAD_QUEUE);

        assertEquals(expectedMessage, messageFromMQ, "Expected message from ActiveMQ to match the created training session");
    }

    @And("Training session appears in the database")
    @Transactional
    public void trainingSessionAppearsInDatabase() {
        Training actual = trainingRepo.findById(scenarioContext.getCurrentTraining().getId()).orElseThrow(() -> new AssertionError("Training not found in database"));
        //Assert that the main fields match
        //It would be pretty complicated to use equals() method here, so we will check each field separately
        assertEquals(scenarioContext.getCurrentTraining().getTrainee().getUsername(), actual.getTrainee().getUser().getUsername(), "Expected trainee username to match");
        assertEquals(scenarioContext.getCurrentTraining().getTrainer().getUsername(), actual.getTrainer().getUser().getUsername(), "Expected trainer username to match");
        assertEquals(scenarioContext.getCurrentTraining().getTrainingDate(), actual.getTrainingDate(), "Expected training date to match");
        assertEquals(scenarioContext.getCurrentTraining().getTrainingDuration(), actual.getTrainingDuration(), "Expected training duration to match");
    }

    @And("I cancel the training session")
    public void cancelTraining() {
        //We received the "ADD" message from previous step, so here's a hack to remove it from the queue
        jmsTemplate.receive(JmsConfig.WORKLOAD_QUEUE);
        //We invoke this step after we created a training session, so we can use the current training from the scenario context
        restTemplate.delete("/trainings/" + scenarioContext.getCurrentTraining().getId());
        //We expect that DELETE message should appear in ActiveMQ, so just change ActionType
        scenarioContext.getCurrentTraining().setActionType("DELETE");
    }

}
