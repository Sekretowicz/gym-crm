package com.sekretowicz.gym_crm;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources",
        glue = "com.sekretowicz.gym_crm"
)
public class CucumberIntegrationTest {
}
