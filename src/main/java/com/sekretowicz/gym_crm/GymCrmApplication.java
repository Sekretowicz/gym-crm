package com.sekretowicz.gym_crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
public class GymCrmApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymCrmApplication.class, args);
	}

}
