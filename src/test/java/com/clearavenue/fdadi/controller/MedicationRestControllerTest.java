package com.clearavenue.fdadi.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;

import com.clearavenue.fdadi.model.AllMedicationsResult;
import com.clearavenue.fdadi.model.AllPharmClassesResult;
import com.clearavenue.fdadi.model.LabelOpenfda;
import com.clearavenue.fdadi.model.LabelResult;
import com.clearavenue.fdadi.model.LabelResults;
import com.clearavenue.fdadi.model.Medication;
import com.clearavenue.fdadi.model.MedicationDetailsResult;
import com.clearavenue.fdadi.model.MedicationResult;
import com.clearavenue.fdadi.model.MedicationsResult;
import com.clearavenue.fdadi.model.PharmClass;
import com.clearavenue.fdadi.service.ApiService;
import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebMvcTest(MedicationRestController.class)
@ActiveProfiles("test")
public class MedicationRestControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	MedicationService medService;

	@MockBean
	PharmClassService pharmService;

	@MockBean
	BuildProperties buildProperties;

	@MockBean
	ApiService api;

	@MockBean
	RestTemplate restTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void allMedicationsNoneTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/allMedications")).andExpect(status().isOk()).andReturn();
		final AllMedicationsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), AllMedicationsResult.class);
		assertThat(actual.getMedications().size()).isEqualTo(0);
	}

	@Test
	public void allMedicationsTest() throws Exception {
		given(medService.findAll()).willReturn(List.of(Medication.builder().id(1L).medicationName("Med1").build(), Medication.builder().id(2L).medicationName("Med2").build()));

		final MvcResult result = mockMvc.perform(get("/allMedications")).andExpect(status().isOk()).andReturn();
		final AllMedicationsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), AllMedicationsResult.class);
		assertThat(actual.getMedications().size()).isEqualTo(2);
	}

	@Test
	public void getMedicationsNoneTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/medication/cipro")).andExpect(status().isOk()).andReturn();
		final MedicationResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationResult.class);
		assertThat(actual.getMedication().isEmpty()).isTrue();
	}

	@Test
	public void getMedicationsTest() throws Exception {
		given(medService.findByMedicationName("cipro")).willReturn(Optional.of(Medication.builder().id(1L).medicationName("cipro").build()));

		final MvcResult result = mockMvc.perform(get("/medication/cipro")).andExpect(status().isOk()).andReturn();
		final MedicationResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationResult.class);
		assertThat(actual.getMedication().isEmpty()).isFalse();
	}

	@Test
	public void getMedicationDetailsNoneTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/medicationDetails/cipro")).andExpect(status().isOk()).andReturn();
		final MedicationDetailsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationDetailsResult.class);
		assertThat(actual.getLabelResult().getMeta().getDisclaimer()).isEqualTo("No Details Found");
	}

	@Test
	public void getMedicationDetailsTest() throws Exception {
		final LabelResults lrs = LabelResults.builder().version("1.0").description(List.of("Used as antibiotic")).adverseReactions(List.of("None")).build();
		given(api.getLabel("cipro")).willReturn(Optional.of(LabelResult.builder().results(List.of(lrs)).build()));

		final MvcResult result = mockMvc.perform(get("/medicationDetails/cipro")).andExpect(status().isOk()).andReturn();
		final MedicationDetailsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationDetailsResult.class);
		assertThat(actual.getLabelResult().getResults().size()).isEqualTo(1);
		assertThat(actual.getLabelResult().getResults().get(0).getAdverseReactions().get(0)).isEqualTo("None");
	}

	@Test
	public void allPharmClassesNoneTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/allPharmClasses")).andExpect(status().isOk()).andReturn();
		final AllPharmClassesResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), AllPharmClassesResult.class);
		assertThat(actual.getPharmClasses().size()).isEqualTo(0);
	}

	@Test
	public void allPharmClassesTest() throws Exception {
		given(pharmService.findAll()).willReturn(List.of(PharmClass.builder().id(1L).pharmClassName("PC1").build(), PharmClass.builder().id(2L).pharmClassName("PC2").build()));

		final MvcResult result = mockMvc.perform(get("/allPharmClasses")).andExpect(status().isOk()).andReturn();
		final AllPharmClassesResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), AllPharmClassesResult.class);
		assertThat(actual.getPharmClasses().size()).isEqualTo(2);
	}

	@Test
	public void getMedicationsByPharmClassNoneTest() throws Exception {
		final MvcResult result = mockMvc.perform(get("/medicationsByPharmClass/antibiotic")).andExpect(status().isOk()).andReturn();
		final MedicationsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationsResult.class);
		assertThat(actual.getMedications().isEmpty()).isTrue();
	}

	@Test
	public void getMedicationsByPharmClassTest() throws Exception {
		final List<LabelResults> lrs = List.of(
				LabelResults.builder().openfda(LabelOpenfda.builder().genericName(List.of("cipro")).brandName(List.of("cipro")).build()).purpose(List.of("infection control"))
						.build(),
				LabelResults.builder().openfda(LabelOpenfda.builder().genericName(List.of("amoxicillian")).brandName(List.of("amoxicillian")).build())
						.purpose(List.of("infection control")).build());

		final LabelResult lr = LabelResult.builder().results(lrs).build();
		given(api.getLabelsByPharmClass("antibiotic")).willReturn(Optional.of(lr));

		final MvcResult result = mockMvc.perform(get("/medicationsByPharmClass/antibiotic")).andExpect(status().isOk()).andReturn();
		final MedicationsResult actual = objectMapper.readValue(result.getResponse().getContentAsString(), MedicationsResult.class);
		assertThat(actual.getMedications().size()).isEqualTo(2);
	}

}
