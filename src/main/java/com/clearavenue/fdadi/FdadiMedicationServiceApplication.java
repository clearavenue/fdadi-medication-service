package com.clearavenue.fdadi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class FdadiMedicationServiceApplication {

	@Bean
	public RestTemplate restTemplate(final RestTemplateBuilder builder) {
		return builder.build();
	}

	@Autowired
	MedicationService medService;

	@Autowired
	PharmClassService pharmService;

	public static void main(final String[] args) {
		SpringApplication.run(FdadiMedicationServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init() {

		return args -> {
			log.info("Adding all medications");
			medService.init();
			log.info("Adding all pharmClasses");
			pharmService.init();
		};

	}
}
