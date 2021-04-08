package com.clearavenue.fdadi;

import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class FdadiMedicationServiceApplication {

    @Bean
    public RestTemplate restTemplate(final RestTemplateBuilder builder) {
        return builder.build();
    }

    private final MedicationService medService;
    private final PharmClassService pharmService;
    private final BuildProperties buildProperties;

	@Bean
	public ApplicationRunner osLogger(Environment environment) {
		return (arguments) -> {
			log.info("Starting {} : {}", buildProperties.getName(), buildProperties.getVersion());
			log.info("Running on {} {} ({})", environment.getProperty("os.name"), environment.getProperty("os.version"), environment.getProperty("os.arch"));
            log.info("Adding all medications");
            medService.init();
            log.info("Adding all pharmClasses");
            pharmService.init();
            log.info("Startup complete.");
		};
	}

    public static void main(final String[] args) {
        SpringApplication.run(FdadiMedicationServiceApplication.class, args);
    }
}
