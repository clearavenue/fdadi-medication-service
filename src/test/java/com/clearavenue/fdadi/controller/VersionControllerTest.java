package com.clearavenue.fdadi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clearavenue.fdadi.service.VersionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import com.clearavenue.fdadi.service.ApiService;
import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;

@WebMvcTest(VersionController.class)
@ActiveProfiles("test")
public class VersionControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MedicationService medService;

	@MockBean
	PharmClassService pharmService;

	@MockBean
	BuildProperties buildProperties;

	@MockBean
	RestTemplateBuilder rtBuilder;

	@MockBean
	VersionService versionService;

	@Test
	public void getVersion() throws Exception {
		mockMvc.perform(get("/version")).andExpect(status().isOk()).andReturn();
	}
}