package com.sekretowicz.gym_crm.utils;

import com.sekretowicz.gym_crm.auth.JwtUtil;
import com.sekretowicz.gym_crm.dto.training.VerboseTrainingResponse;
import com.sekretowicz.gym_crm.dto_legacy.ShortTraineeDto;
import com.sekretowicz.gym_crm.dto_legacy.ShortTrainerDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ScenarioContext {
    @Autowired
    private TestDataHelper testDataHelper;
    @Autowired
    private JwtUtil jwtUtil;

    private ShortTraineeDto currentTrainee = null;
    private ShortTrainerDto currentTrainer = null;

    private Long trainingTypeId = null;
    @Getter
    @Setter
    private VerboseTrainingResponse currentTraining = null;
    @Getter
    @Setter
    private HttpStatus lastResponseStatus = null;
    @Getter
    @Setter
    private String jwtToken = null;
    @Getter
    @Setter
    private HttpHeaders headers = null;

    public void prepareTestData() {
        //By now we need just to create some training types, the rest of the data will be created on demand
        testDataHelper.seedTrainingTypesIfNeeded();

    }

    public ShortTraineeDto getCurrentTrainee() {
        if (currentTrainee == null) {
            currentTrainee = testDataHelper.createRandomTrainee();
        }
        return currentTrainee;
    }

    public ShortTrainerDto getCurrentTrainer() {
        if (currentTrainer == null) {
            currentTrainer = testDataHelper.createRandomTrainer();
        }
        return currentTrainer;
    }

    public Long getTrainingTypeId() {
        if (trainingTypeId == null) {
            testDataHelper.seedTrainingTypesIfNeeded();
            trainingTypeId = 0L;    //Assuming the first training type is the default one
        }
        return trainingTypeId;
    }

    public String getJwtTokenForCurrentUser() {
        if (jwtToken == null) {
            jwtToken = jwtUtil.generateToken(this.getCurrentTrainer().getUsername());
        }
        return jwtToken;
    }

    public HttpHeaders getHeadersForCurrentUser() {
        if (headers == null) {
            headers = new HttpHeaders();
            headers.setBearerAuth(getJwtTokenForCurrentUser());
            headers.setContentType(MediaType.APPLICATION_JSON);
        }
        return headers;
    }
}
