package com.sekretowicz.gym_crm.runners;

import com.sekretowicz.gym_crm.TestConfiguration;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
classes = TestConfiguration.class)
public class SpringIntegrationTest {
    // executeGet implementation
}
