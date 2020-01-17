package com.clearavenue.fdadi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.clearavenue.fdadi.model.AllMedicationsResult;
import com.clearavenue.fdadi.model.AllPharmClassesResult;
import com.clearavenue.fdadi.model.LabelResult;
import com.clearavenue.fdadi.model.Medication;
import com.clearavenue.fdadi.model.MedicationDetailsResult;
import com.clearavenue.fdadi.model.MedicationResult;
import com.clearavenue.fdadi.model.MedicationsResult;
import com.clearavenue.fdadi.model.Meta;
import com.clearavenue.fdadi.service.ApiService;
import com.clearavenue.fdadi.service.MedicationService;
import com.clearavenue.fdadi.service.PharmClassService;

@RestController
public class MedicationRestController {

	@Autowired
	MedicationService medService;

	@Autowired
	PharmClassService pharmService;

	@Autowired
	ApiService api;

	@GetMapping("/allMedications")
	public AllMedicationsResult getAllMedications() {
		return AllMedicationsResult.builder().medications(medService.findAll()).build();
	}

	@GetMapping("/medication/{medicationName}")
	public MedicationResult getMedication(@PathVariable final String medicationName) {
		return MedicationResult.builder().medication(medService.findByMedicationName(medicationName)).build();
	}

	@GetMapping("/medicationDetails/{medicationName}")
	public MedicationDetailsResult getMedicationDetails(@PathVariable final String medicationName) {
		LabelResult result = LabelResult.builder().meta(Meta.builder().disclaimer("No Details Found").build()).build();

		final Optional<LabelResult> label = api.getLabel(medicationName);
		if (label.isPresent()) {
			result = label.get();
		}

		return MedicationDetailsResult.builder().labelResult(result).build();
	}

	@GetMapping("/allPharmClasses")
	public AllPharmClassesResult getAllPharmClasses() {
		return AllPharmClassesResult.builder().pharmClasses(pharmService.findAll()).build();
	}

	@GetMapping("/medicationsByPharmClass/{pharmClassName}")
	public MedicationsResult getMedicationsByPharmClass(@PathVariable final String pharmClassName) {
		final List<Medication> medicationsList = new ArrayList<>();

		final Optional<LabelResult> label = api.getLabelsByPharmClass(pharmClassName);
		if (label.isPresent()) {
			LabelResult result = LabelResult.builder().meta(Meta.builder().disclaimer("No Details Found for Selected PharmClass").build()).build();
			result = label.get();
			result.results.forEach(l -> {
				l.getOpenfda().getBrandName().stream().map(bn -> Medication.builder().medicationName(bn.toUpperCase(Locale.getDefault())).build()).forEach(medicationsList::add);
				l.getOpenfda().getGenericName().stream().map(gn -> Medication.builder().medicationName(gn.toUpperCase(Locale.getDefault())).build()).forEach(medicationsList::add);
			});
		}

		return MedicationsResult.builder().medications(medicationsList.stream().distinct().collect(Collectors.toList())).build();
	}

}
