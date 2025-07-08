package com.sekretowicz.gym_crm;

import com.sekretowicz.gym_crm.utils.ScenarioContext;
import com.sekretowicz.gym_crm.utils.TestDataHelper;
import org.springframework.context.annotation.Bean;

//It's needed because we can't autowire test classes directly in Cucumber steps

@org.springframework.boot.test.context.TestConfiguration
public class TestConfiguration {
    @Bean
    public ScenarioContext scenarioContext() {
        return new ScenarioContext();
    }

    @Bean
    public TestDataHelper testDataHelper() {
        return new TestDataHelper();
    }
}
